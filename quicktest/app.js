'use strict';

const ENDPOINTS = [
    { method: 'GET', path: '/games', desc: 'list games' },
    { method: 'GET', path: '/games/{id}', desc: 'one game by id', needsId: true },
    { method: 'GET', path: '/games/users', desc: 'all players' },
    { method: 'GET', path: '/games/count', desc: 'number of games' },
    { method: 'POST', path: '/dispatch/refresh', desc: 'start game import (pullGames.py)' },
    { method: 'POST', path: '/dispatch/analysis', desc: 'analyze pending games (analyzer.py)' },
    { method: 'POST', path: '/games/{id}/analysis-request', desc: 'mark game for analysis', needsId: true },
];

const $ = (sel) => document.querySelector(sel);
const getListEl = $('#getList');
const postListEl = $('#postList');
const methodBadge = $('#methodBadge');
const pathDisplay = $('#pathDisplay');
const idParamEl = $('#idParam');
const idValueEl = $('#idValue');
const bodyBlockEl = $('#bodyBlock');
const reqBodyEl = $('#reqBody');
const sendBtn = $('#sendBtn');
const errBox = $('#errBox');
const respBodyEl = $('#respBody');
const respMeta = $('#respMeta');
const baseUrlEl = $('#baseUrl');
const statusEl = $('#apiStatus');
const statusText = $('#statusText');

let active = ENDPOINTS[0];
let controller = new AbortController();

function baseUrl() {
    return baseUrlEl.value.trim().replace(/\/+$/, '');
}

function fullPath(ep) {
    let p = ep.path;
    if (ep.needsId) {
        const id = idValueEl.value.trim() || '{id}';
        p = p.replace('{id}', id);
    }
    return baseUrl() + p;
}

/* ---------- sidebar ---------- */

function renderLists() {
    const getEls = ENDPOINTS.filter((e) => e.method === 'GET');
    const postEls = ENDPOINTS.filter((e) => e.method === 'POST');
    getListEl.innerHTML = '';
    postListEl.innerHTML = '';

    [...getEls, ...postEls].forEach((ep) => {
        const li = document.createElement('li');
        const btn = document.createElement('button');
        btn.dataset.path = ep.path;
        btn.innerHTML = `<span class="method-tag ${ep.method.toLowerCase()}">${ep.method}</span><span>${ep.path}</span>`;
        btn.title = ep.desc;
        btn.addEventListener('click', () => selectEndpoint(ep));
        li.appendChild(btn);
        if (ep.method === 'GET') getListEl.appendChild(li);
        else postListEl.appendChild(li);
    });
    rerenderActive();
}

function rerenderActive() {
    document.querySelectorAll('.endpoint-list button').forEach((b) => {
        b.classList.toggle('active', b.dataset.path === active.path);
    });

    methodBadge.textContent = active.method;
    methodBadge.className = `method ${active.method.toLowerCase()}`;
    pathDisplay.textContent = fullPath(active);

    idParamEl.classList.toggle('hidden', !active.needsId);
    bodyBlockEl.classList.toggle('hidden', !active.needsBody);

    errBox.classList.add('hidden');
}

function selectEndpoint(ep) {
    active = ep;
    rerenderActive();
}

/* ---------- syntax-highlighted JSON ---------- */

function escapeHtml(s) {
    return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

function prettyJson(value) {
    return JSON.stringify(value, null, 2);
}

function highlightJson(str) {
    return escapeHtml(str).replace(
        /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d+)?(?:[eE][+-]?\d+)?)/g,
        (match) => {
            let cls = 'num';
            if (match.startsWith('"')) {
                cls = /:$/.test(match) ? 'key' : 'str';
            } else if (/true|false/.test(match)) {
                cls = 'bool';
            } else if (/null/.test(match)) {
                cls = 'null';
            }
            return `<span class="${cls}">${match.replace(/:$/, ':')}</span>`;
        }
    );
}

/* ---------- request execution ---------- */

function showErr(msg) {
    errBox.textContent = msg;
    errBox.classList.remove('hidden');
}

async function doRequest() {
    controller.abort();
    controller = new AbortController();

    const ep = active;
    sendBtn.classList.add('loading');
    sendBtn.textContent = '…';
    errBox.classList.add('hidden');

    const opts = { method: ep.method, signal: controller.signal, headers: {} };

    if (ep.needsBody) {
        let bodyText = reqBodyEl.value.trim();
        if (!bodyText) {
            showErr('Empty request body for POST. Fill some JSON or use {}.');
            sendBtn.classList.remove('loading');
            sendBtn.textContent = 'Run →';
            return;
        }
        try {
            JSON.parse(bodyText);
        } catch (e) {
            showErr(`Invalid JSON body: ${e.message}`);
            sendBtn.classList.remove('loading');
            sendBtn.textContent = 'Run →';
            return;
        }
        opts.headers['Content-Type'] = 'application/json';
        opts.body = bodyText;
    }

    if (ep.needsId && (!idValueEl.value.trim() || /\D/.test(idValueEl.value.trim()))) {
        showErr('A numeric id is required for this endpoint.');
        sendBtn.classList.remove('loading');
        sendBtn.textContent = 'Run →';
        return;
    }

    const url = fullPath(ep);
    const t0 = performance.now();

    try {
        const res = await fetch(url, opts);
        const ms = Math.round(performance.now() - t0);
        const ctype = res.headers.get('content-type') || '';

        let data;
        let raw = '';
        if (res.status === 204) {
            data = null;
        } else if (ctype.includes('application/json')) {
            raw = await res.text();
            try {
                data = prettyJson(JSON.parse(raw));
            } catch {
                data = raw;
            }
        } else {
            data = await res.text();
        }

        const size = raw ? `${(raw.length / 1024).toFixed(1)} KB` : '—';

        respBodyEl.innerHTML = data == null
            ? '<span class="pun">null</span>'
            : highlightJson(typeof data === 'string' ? data : prettyJson(data));

        let isHtml = false;
        if (ctype.includes('text/html')) {
            isHtml = true;
            respBodyEl.innerHTML = escapeHtml(data);
        }

        const cls = res.ok ? 'code-2xx' : res.status >= 500 ? 'code-5xx' : 'code-4xx';
        respMeta.innerHTML =
            `<b class="${cls}">${res.status} ${res.statusText}</b>` +
            ` &middot; ${ms} ms &middot; ${isHtml ? 'html' : ctype.includes('json') ? 'json' : 'text'} &middot; ${size}`;
        respBodyEl.classList.toggle('is-plain', isHtml);
    } catch (e) {
        if (e.name === 'AbortError') return;
        const ms = Math.round(performance.now() - t0);
        respMeta.innerHTML = `<b class="code-5xx">network error</b> &middot; ${ms} ms`;
        respBodyEl.innerHTML =
            `<span class="placeholder">Could not reach <strong>${escapeHtml(url)}</strong>.\n` +
            `Is the Spring Boot API running? Hint: start it with <strong>./chess.sh start</strong>.\n\n${escapeHtml(e.message)}</span>`;
    } finally {
        sendBtn.classList.remove('loading');
        sendBtn.textContent = 'Run →';
    }
}

/* ---------- run all GETs ---------- */

async function runAllGets() {
    active = ENDPOINTS[0];
    rerenderActive();

    const results = {};
    const metas = [];

    for (const ep of ENDPOINTS.filter((e) => e.method === 'GET' && !e.needsId)) {
        try {
            const res = await fetch(baseUrl() + ep.path, { signal: controller.signal });
            const ok = res.ok;
            let body = null;
            try { body = await res.json(); } catch { /* keep null */ }
            results[ep.path] = ok ? body : { error: `${res.status} ${res.statusText}` };
            metas.push(`${ep.path} → ${res.status}`);
        } catch (e) {
            if (e.name === 'AbortError') return;
            results[ep.path] = { error: e.message };
            metas.push(`${ep.path} → failed`);
        }
    }

    respBodyEl.innerHTML = highlightJson(prettyJson(results));
    respMeta.innerHTML = `<b class="code-2xx">all GETs</b> &middot; ${metas.join(' · ')}`;
}

/* ---------- API health check ---------- */

async function checkHealth() {
    statusEl.className = 'status-pill checking';
    statusText.textContent = 'checking…';
    try {
        const res = await fetch(baseUrl() + '/games/count', { signal: controller.signal });
        if (res.ok) {
            statusEl.className = 'status-pill ok';
            statusText.textContent = 'API online';
        } else {
            statusEl.className = 'status-pill down';
            statusText.textContent = `API ${res.status}`;
        }
    } catch {
        statusEl.className = 'status-pill down';
        statusText.textContent = 'API offline';
    }
}

/* ---------- wire up ---------- */

baseUrlEl.addEventListener('change', () => {
    rerenderActive();
    checkHealth();
});
baseUrlEl.addEventListener('input', () => {
    rerenderActive();
    clearTimeout(baseUrlEl._t);
    baseUrlEl._t = setTimeout(checkHealth, 600);
});

idValueEl.addEventListener('input', rerenderActive);
sendBtn.addEventListener('click', doRequest);
pathDisplay.addEventListener('click', () => navigator.clipboard?.writeText(pathDisplay.textContent));
refreshAll.addEventListener('click', runAllGets);
sendBtn.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') doRequest();
});

document.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' && (e.target === idValueEl || e.target === baseUrlEl)) {
        if (e.target === idValueEl) doRequest();
        else checkHealth();
    }
    if (e.key === 'F5') { e.preventDefault(); runAllGets(); }
});

renderLists();
checkHealth();