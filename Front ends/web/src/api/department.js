import request from '@/utils/request'
import da from 'element-ui/src/locale/lang/da'
export function getDepartmentPages(page,size) {
  return request({
    url: '/department/page',
    method: 'post',
    data:{
      "page":page,
      "size":size,
    }
  })
}


export function addDepartment(data) {
  return request({
    url: '/department/add',
    method: 'post',
    data:data
  })
}

export function updateDepartment(data) {
  return request({
    url: '/department/update',
    method: 'post',
    data:data
  })
}
export function delDepartment(id) {
  return request({
    url: '/department/del?id='+id,
    method: 'get'
  })
}



export function getDepartmentNames() {
  return request({
    url: '/department/names',
    method: 'get'
  })
}


export function newsByType() {
  return request({
    url: '/api/news/newsByType',
    method: 'POST'
  })
}
export function distribution() {
  return request({
    url: '/api/news/distribution',
    method: 'POST'
  })
}
