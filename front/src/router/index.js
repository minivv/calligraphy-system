import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/register', component: () => import('../views/Register.vue') },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/home',
    children: [
      { path: 'home', component: () => import('../views/Home.vue') },
      { path: 'works', component: () => import('../views/WorkList.vue') },
      { path: 'work/:id', component: () => import('../views/WorkDetail.vue') },
      { path: 'activities', component: () => import('../views/ActivityList.vue') },
      { path: 'activity/:id', component: () => import('../views/ActivityDetail.vue') },
      { path: 'videos', component: () => import('../views/VideoList.vue') },
      { path: 'video/:id', component: () => import('../views/VideoDetail.vue') },
      { path: 'votes', component: () => import('../views/VoteList.vue') },
      { path: 'vote/:id', component: () => import('../views/VoteDetail.vue') },
      { path: 'profile', component: () => import('../views/Profile.vue'), meta: { auth: true } },
      { path: 'my-works', component: () => import('../views/MyWorks.vue'), meta: { auth: true } },
      { path: 'my-registrations', component: () => import('../views/MyRegistrations.vue'), meta: { auth: true } },
      { path: 'my-complaints', component: () => import('../views/MyComplaints.vue'), meta: { auth: true } }
    ]
  }
]

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta && to.meta.auth) {
    const user = localStorage.getItem('user')
    if (!user) {
      return next('/login')
    }
  }
  next()
})

export default router
