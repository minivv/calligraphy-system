import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/Dashboard.vue') },
      { path: 'users', component: () => import('../views/Users.vue') },
      { path: 'admins', component: () => import('../views/Admins.vue') },
      { path: 'works', component: () => import('../views/Works.vue') },
      { path: 'activities', component: () => import('../views/Activities.vue') },
      { path: 'registrations', component: () => import('../views/Registrations.vue') },
      { path: 'videos', component: () => import('../views/Videos.vue') },
      { path: 'votes', component: () => import('../views/Votes.vue') },
      { path: 'complaints', component: () => import('../views/Complaints.vue') }
    ]
  }
]

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') return next()
  const admin = localStorage.getItem('admin')
  if (!admin) return next('/login')
  next()
})

export default router
