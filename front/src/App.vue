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
/* ========== Airtable Design System Tokens ========== */
:root {
  --color-primary: #1b61c9;
  --color-primary-hover: #1550a8;
  --color-primary-light: rgba(27,97,201,0.08);
  --color-text: #181d26;
  --color-text-secondary: #333333;
  --color-text-weak: rgba(4,14,32,0.69);
  --color-text-muted: rgba(4,14,32,0.50);
  --color-surface: #ffffff;
  --color-surface-subtle: #f8fafc;
  --color-surface-hover: #f0f4f8;
  --color-border: #e0e2e6;
  --color-border-light: #ebeef5;
  --color-success: #006400;
  --color-danger: #d63031;
  --color-warning: #e6a23c;

  --shadow-card: 0px 0px 1px rgba(0,0,0,0.32), 0px 0px 2px rgba(0,0,0,0.08), 0px 1px 3px rgba(45,127,249,0.28), 0px 0px 0px 0.5px rgba(0,0,0,0.06) inset;
  --shadow-soft: 0px 0px 20px rgba(15,48,106,0.05);
  --shadow-hover: 0px 2px 8px rgba(45,127,249,0.22), 0px 0px 1px rgba(0,0,0,0.24);

  --radius-sm: 2px;
  --radius-button: 12px;
  --radius-card: 16px;
  --radius-section: 24px;
  --radius-lg: 32px;

  --font-family: -apple-system, system-ui, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  --letter-spacing-body: 0.18px;
  --letter-spacing-caption: 0.14px;
  --letter-spacing-button: 0.08px;
}

/* ========== Base Reset ========== */
* { margin: 0; padding: 0; box-sizing: border-box; }
html, body, #app { height: 100%; }
body {
  background: var(--color-surface-subtle);
  font-family: var(--font-family);
  color: var(--color-text);
  letter-spacing: var(--letter-spacing-body);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

a { color: var(--color-primary); text-decoration: none; }
a:hover { color: var(--color-primary-hover); }

/* ========== Element UI Global Overrides ========== */

/* Buttons */
.el-button--primary {
  background-color: var(--color-primary) !important;
  border-color: var(--color-primary) !important;
  border-radius: var(--radius-button) !important;
  letter-spacing: var(--letter-spacing-button);
  font-weight: 500;
}
.el-button--primary:hover,
.el-button--primary:focus {
  background-color: var(--color-primary-hover) !important;
  border-color: var(--color-primary-hover) !important;
}
.el-button--default {
  border-radius: var(--radius-button) !important;
  border-color: var(--color-border) !important;
  color: var(--color-text) !important;
  letter-spacing: var(--letter-spacing-button);
}
.el-button--default:hover,
.el-button--default:focus {
  color: var(--color-primary) !important;
  border-color: var(--color-primary) !important;
  background-color: var(--color-primary-light) !important;
}
.el-button--warning {
  border-radius: var(--radius-button) !important;
}
.el-button--danger {
  border-radius: var(--radius-button) !important;
}
.el-button--text {
  color: var(--color-primary) !important;
  letter-spacing: var(--letter-spacing-button);
}
.el-button--text:hover {
  color: var(--color-primary-hover) !important;
}

/* Cards */
.el-card {
  border-radius: var(--radius-card) !important;
  border: 1px solid var(--color-border) !important;
  box-shadow: var(--shadow-soft) !important;
  transition: box-shadow 0.25s ease, transform 0.25s ease;
}
.el-card:hover {
  box-shadow: var(--shadow-card) !important;
}
.el-card__header {
  border-bottom: 1px solid var(--color-border-light) !important;
  padding: 16px 24px !important;
  font-weight: 500;
  color: var(--color-text);
}
.el-card__body {
  padding: 24px !important;
}

/* Inputs */
.el-input__inner {
  border-radius: var(--radius-button) !important;
  border-color: var(--color-border) !important;
  color: var(--color-text) !important;
  letter-spacing: var(--letter-spacing-body);
}
.el-input__inner:focus {
  border-color: var(--color-primary) !important;
  box-shadow: 0 0 0 2px rgba(27,97,201,0.12) !important;
}
.el-textarea__inner {
  border-radius: var(--radius-button) !important;
  border-color: var(--color-border) !important;
  color: var(--color-text) !important;
  letter-spacing: var(--letter-spacing-body);
}
.el-textarea__inner:focus {
  border-color: var(--color-primary) !important;
  box-shadow: 0 0 0 2px rgba(27,97,201,0.12) !important;
}
.el-select .el-input__inner {
  border-radius: var(--radius-button) !important;
}

/* Tags */
.el-tag {
  border-radius: 8px !important;
  letter-spacing: var(--letter-spacing-caption);
}
.el-tag--success {
  background-color: rgba(0,100,0,0.08) !important;
  border-color: rgba(0,100,0,0.16) !important;
  color: var(--color-success) !important;
}

/* Pagination */
.el-pagination .btn-prev,
.el-pagination .btn-next {
  border-radius: 8px !important;
}
.el-pagination .el-pager li {
  border-radius: 8px !important;
}
.el-pagination .el-pager li.active {
  background-color: var(--color-primary) !important;
}

/* Dialog */
.el-dialog {
  border-radius: var(--radius-section) !important;
}
.el-dialog__header {
  padding: 24px 24px 16px !important;
}
.el-dialog__title {
  color: var(--color-text) !important;
  font-weight: 500;
  font-size: 18px;
}
.el-dialog__body {
  padding: 16px 24px !important;
}
.el-dialog__footer {
  padding: 12px 24px 24px !important;
}

/* Menu */
.el-menu--horizontal > .el-menu-item {
  border-radius: 8px;
  margin: 0 2px;
  transition: background-color 0.2s;
}

/* Divider */
.el-divider {
  border-top-color: var(--color-border-light) !important;
}

/* Table */
.el-table {
  border-radius: var(--radius-button) !important;
  overflow: hidden;
}
.el-table th {
  background-color: var(--color-surface-subtle) !important;
  color: var(--color-text) !important;
  font-weight: 500 !important;
  letter-spacing: var(--letter-spacing-caption);
}
.el-table td {
  color: var(--color-text-secondary) !important;
  letter-spacing: var(--letter-spacing-caption);
}

/* Progress bar */
.el-progress-bar__inner {
  background-color: var(--color-primary) !important;
}

/* Switch */
.el-switch.is-checked .el-switch__core {
  background-color: var(--color-primary) !important;
  border-color: var(--color-primary) !important;
}

/* Avatar */
.el-avatar {
  border: 2px solid var(--color-border);
}

/* Form label */
.el-form-item__label {
  color: var(--color-text) !important;
  font-weight: 500;
  letter-spacing: var(--letter-spacing-caption);
}

/* Dropdown */
.el-dropdown-menu__item:hover {
  background-color: var(--color-primary-light) !important;
  color: var(--color-primary) !important;
}

/* Radio */
.el-radio__input.is-checked .el-radio__inner {
  background-color: var(--color-primary) !important;
  border-color: var(--color-primary) !important;
}
.el-radio__input.is-checked + .el-radio__label {
  color: var(--color-primary) !important;
}
</style>
