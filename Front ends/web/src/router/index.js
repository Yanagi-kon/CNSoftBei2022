import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: 'Dashboard', icon: 'dashboard' }
    }]
  },


  {
    path: '/basic',
    component: Layout,
    redirect: '/basic/user',
    name: 'Basic',
    meta: {
      title: '',
      icon: 'basic'
    },
    children: [
      {
        path: 'user',
        component: () => import('@/views/basic/User'), // Parent router-view
        name: 'User',
        meta: { title: '用户列表' },
        children: []
      },
      {
        path: 'newsManager',
        component: () => import('@/views/basic/NewsManager'), // Parent router-view
        name: 'NewsManager',
        meta: { title: '新闻管理' },
        children: []
      },
    ]
  },

  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    name: 'System',
    meta: {
      title: 'System',
      icon: 'system'
    },
    children: [
      {
        path: 'notice',
        component: () => import('@/views/system/Notice'),
        name: 'Notice',
        meta: { title: 'notice' },
        children: []
      },
      {
        path: 'user',
        component: () => import('@/views/system/User'),
        name: 'User',
        meta: { title: 'user' },
        children: []
      },
      {
        path: 'admin',
        component: () => import('@/views/system/Admin'), // Parent router-view
        name: 'Admin',
        meta: { title: 'admin' },
        children: []
      },
    ]
  },
  {
    path: '/statistics',
    component: Layout,
    redirect: '/statistics/Statistics',
    name: 'statistics',
    meta: {
      title: '',
      icon: 'system'
    },
    children: [
      {
        path: 'statistics',
        component: () => import('@/views/statistics/Statistics'),
        name: 'Statistics',
        meta: { title: '统计' },
        children: []
      },
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
