import request from '@/utils/request'
import da from 'element-ui/src/locale/lang/da'
export function getMoneyInfoPages(page,size) {
  return request({
    url: '/moneyinfo/page',
    method: 'post',
    data:{
      "page":page,
      "size":size,
    }
  })
}


export function addMoneyInfo(data) {
  return request({
    url: '/moneyinfo/add',
    method: 'post',
    data:data
  })
}

export function updateMoneyInfo(data) {
  return request({
    url: '/moneyinfo/update',
    method: 'post',
    data:data
  })
}
export function delMoneyInfo(id) {
  return request({
    url: '/moneyinfo/del?id='+id,
    method: 'get'
  })
}
