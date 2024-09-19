import request from '@/utils/request'
import da from 'element-ui/src/locale/lang/da'

export function getMoneyTypes() {
  return request({
    url: '/moneytype/all',
    method: 'get'
  })
}


export function addMoneyTypes(data) {
  return request({
    url: '/moneytype/add',
    method: 'post',
    data:data
  })
}

export function updateMoneyTypes(data) {
  return request({
    url: '/moneytype/update',
    method: 'post',
    data:data
  })
}

export function delMoneyTypes(id) {
  return request({
    url: '/moneytype/del?id='+id,
    method: 'get'
  })
}
