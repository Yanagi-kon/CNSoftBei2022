import request from '@/utils/request'
import da from 'element-ui/src/locale/lang/da'
export function getNoticePages(page,size) {
  return request({
    url: '/notice/page',
    method: 'post',
    data:{
      "page":page,
      "size":size,
    }
  })
}


export function addNotice(data) {
  return request({
    url: '/notice/add',
    method: 'post',
    data:data
  })
}

export function updateNotice(data) {
  return request({
    url: '/notice/update',
    method: 'post',
    data:data
  })
}
export function delNotice(id) {
  return request({
    url: '/notice/del?id='+id,
    method: 'get'
  })
}
