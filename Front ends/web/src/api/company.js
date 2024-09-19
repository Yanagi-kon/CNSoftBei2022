import request from '@/utils/request'
import da from 'element-ui/src/locale/lang/da'

export function getInfo() {
  return request({
    url: '/company/info',
    method: 'get'
  })
}
