<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
:root {
  /* Binance.US Color Tokens */
  --color-primary: #F0B90B;
  --color-primary-hover: #D0980B;
  --color-primary-gold: #FFD000;
  --color-primary-light: #F8D12F;
  --color-focus: #1EAEDB;

  --color-text: #1E2026;
  --color-text-secondary: #32313A;
  --color-text-slate: #848E9C;
  --color-text-steel: #686A6C;
  --color-text-muted: #777E90;
  --color-text-hover: #1A1A1A;

  --color-surface: #FFFFFF;
  --color-surface-snow: #F5F5F5;
  --color-dark: #222126;
  --color-dark-card: #2B2F36;
  --color-ink: #1E2026;

  --color-success: #0ECB81;
  --color-danger: #F6465D;
  --color-border: #E6E8EA;
  --color-border-gold: #FFD000;

  /* Shadows */
  --shadow-card: rgba(32, 32, 37, 0.05) 0px 3px 5px 0px;
  --shadow-card-hover: rgba(8, 8, 8, 0.05) 0px 3px 5px 5px;
  --shadow-pill: rgb(153, 153, 153) 0px 2px 10px -3px;

  /* Border Radius */
  --radius-sm: 6px;
  --radius-input: 8px;
  --radius-card: 12px;
  --radius-lg: 24px;
  --radius-pill: 50px;

  /* Font */
  --font-family: 'BinancePlex', Arial, sans-serif;
  --letter-spacing-button: 0.16px;
}

* { margin: 0; padding: 0; box-sizing: border-box; }
html, body, #app { height: 100%; }
body {
  font-family: var(--font-family);
  color: var(--color-text);
  -webkit-font-smoothing: antialiased;
}

/* ===== Element UI Global Overrides ===== */

/* Buttons */
.el-button--primary {
  background-color: var(--color-primary) !important;
  border-color: var(--color-primary) !important;
  color: var(--color-ink) !important;
  border-radius: var(--radius-sm) !important;
  font-weight: 600 !important;
  font-family: var(--font-family) !important;
  letter-spacing: var(--letter-spacing-button) !important;
  transition: background 200ms ease !important;
}
.el-button--primary:hover,
.el-button--primary:focus {
  background-color: var(--color-focus) !important;
  border-color: var(--color-focus) !important;
  color: #FFFFFF !important;
}
.el-button--primary:active {
  background-color: var(--color-primary-hover) !important;
  border-color: var(--color-primary-hover) !important;
}
.el-button--default {
  border-color: var(--color-border) !important;
  color: var(--color-text) !important;
  border-radius: var(--radius-sm) !important;
  font-weight: 600 !important;
  font-family: var(--font-family) !important;
}
.el-button--default:hover,
.el-button--default:focus {
  border-color: var(--color-primary) !important;
  color: var(--color-primary) !important;
}
.el-button--danger {
  background-color: var(--color-danger) !important;
  border-color: var(--color-danger) !important;
  border-radius: var(--radius-sm) !important;
}
.el-button--warning {
  background-color: #E6A23C !important;
  border-color: #E6A23C !important;
  border-radius: var(--radius-sm) !important;
}
.el-button--text {
  color: var(--color-primary) !important;
  font-weight: 600 !important;
}
.el-button--text:hover {
  color: var(--color-primary-hover) !important;
}

/* Cards */
.el-card {
  border-radius: var(--radius-card) !important;
  border: 1px solid var(--color-border) !important;
  box-shadow: var(--shadow-card) !important;
}

/* Inputs */
.el-input__inner {
  border-radius: var(--radius-input) !important;
  border-color: var(--color-border) !important;
  font-family: var(--font-family) !important;
  color: var(--color-text) !important;
  transition: border-color 200ms ease !important;
}
.el-input__inner:focus {
  border-color: #000000 !important;
}
.el-input__inner::placeholder {
  color: var(--color-text-slate) !important;
}
.el-textarea__inner {
  border-radius: var(--radius-input) !important;
  border-color: var(--color-border) !important;
  font-family: var(--font-family) !important;
  color: var(--color-text) !important;
}
.el-textarea__inner:focus {
  border-color: #000000 !important;
}
.el-select .el-input__inner {
  border-radius: var(--radius-input) !important;
}

/* Tags */
.el-tag {
  border-radius: var(--radius-sm) !important;
  font-family: var(--font-family) !important;
  font-weight: 600 !important;
  font-size: 12px !important;
}
.el-tag--success {
  background-color: rgba(14,203,129,0.1) !important;
  border-color: rgba(14,203,129,0.2) !important;
  color: var(--color-success) !important;
}
.el-tag--danger {
  background-color: rgba(246,70,93,0.1) !important;
  border-color: rgba(246,70,93,0.2) !important;
  color: var(--color-danger) !important;
}
.el-tag--warning {
  background-color: rgba(240,185,11,0.1) !important;
  border-color: rgba(240,185,11,0.2) !important;
  color: var(--color-primary-hover) !important;
}
.el-tag--info {
  background-color: rgba(132,142,156,0.1) !important;
  border-color: rgba(132,142,156,0.2) !important;
  color: var(--color-text-slate) !important;
}

/* Pagination */
.el-pagination {
  font-family: var(--font-family) !important;
  font-weight: 500 !important;
}
.el-pagination .btn-prev,
.el-pagination .btn-next {
  border-radius: var(--radius-sm) !important;
}
.el-pagination .el-pager li {
  border-radius: var(--radius-sm) !important;
  font-weight: 600 !important;
}
.el-pagination .el-pager li.active {
  background-color: var(--color-primary) !important;
  color: var(--color-ink) !important;
}

/* Dialog */
.el-dialog {
  border-radius: var(--radius-card) !important;
}
.el-dialog__header {
  border-bottom: 1px solid var(--color-border) !important;
  padding: 16px 24px !important;
}
.el-dialog__title {
  font-weight: 600 !important;
  font-size: 16px !important;
  color: var(--color-text) !important;
}
.el-dialog__body {
  padding: 24px !important;
}
.el-dialog__footer {
  border-top: 1px solid var(--color-border) !important;
  padding: 12px 24px !important;
}

/* Table */
.el-table {
  font-family: var(--font-family) !important;
  color: var(--color-text) !important;
}
.el-table th {
  background-color: var(--color-surface-snow) !important;
  color: var(--color-text) !important;
  font-weight: 600 !important;
  font-size: 14px !important;
}
.el-table td {
  font-size: 14px !important;
}
.el-table--striped .el-table__body tr.el-table__row--striped td {
  background: var(--color-surface-snow) !important;
}

/* Menu - sidebar */
.el-menu {
  font-family: var(--font-family) !important;
}

/* Form */
.el-form-item__label {
  font-weight: 500 !important;
  color: var(--color-text) !important;
}

/* Dropdown */
.el-dropdown-menu__item:hover {
  background-color: var(--color-surface-snow) !important;
  color: var(--color-primary) !important;
}
</style>
