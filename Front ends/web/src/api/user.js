import request from '@/utils/request'
import qs from 'qs';
import da from 'element-ui/src/locale/lang/da'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}


export function getMenus() {
  return new Promise((resolve, reject) => {
    request({
      url: '/menu/menus',
      method: 'get'
    }).then(response => {
      const { data } = response
      resolve(data)
    }).catch(error => {
      reject(error)
    })
  })
}


export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get',
  });
}

export function updatePassword(data) {
  return request({
    url: '/user/updatePassword',
    method: 'post',
    data:data
  });
}

export function updateUser(data) {
  return request({
    url: '/user/updateInfo',
    method: 'post',
    data:data
  });
}

export function updateUser2(data) {
  return request({
    url: '/user/update',
    method: 'post',
    data:data
  });
}



export function getUserPages(page,size) {
  return request({
    url: '/user/page',
    method: 'post',
    data:{
      "page":page,
      "size":size,
    }
  })
}


export function addUser(data) {
  return request({
    url: '/user/add',
    method: 'post',
    data:data
  })
}

export function delNews(data) {
  return request({
    url: '/api/news/delNews',
    method: 'post',
    data:data
  })
}
export function addNews(data) {
  return request({
    url: '/api/news/addNews',
    method: 'post',
    data:data
  })
}
export function getNewsPage(data){
  return request({
    url: '/api/backend/news/newsList',
    method: 'post',
    data:data,
  })
}

export function getNewsPageWeb(data){
  return request({
    url: '/api/backend/news/newsList',
    method: 'post',
    data:data,
  })
}

export function updNews(data) {
  return request({
    url: '/api/news/updateNews',
    method: 'post',
    data:data,
  })
}
export function getUserList(data) {
  return request({
    url: '/api/backend/user/getUser',
    method: 'post',
    data:data,
  })
}
export function delSysUser(id) {
  return request({
    url: '/user/del?id=' + id,
    method: 'get'
  })
}
export function delUser(data) {
  return request({
    url: '/api/user/delUser',
    method: 'post',
    data:data,
  })
}


export function webHistory(data) {
  return request({
    url: '/api/backend/user/webHistory',
    method: 'post',
    data:data,
  })
}
export function wordcloud(data) {
  return request({
    url: '/api/user/wordcloud?uid='+data,
    method: 'post',
  })
}


