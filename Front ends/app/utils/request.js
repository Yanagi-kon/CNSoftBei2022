/* 
  http 请求封装
  @author maybe
 */

// uni 自带集成的flyio请求框架
import Fly from 'flyio/dist/npm/wx'
// 创建新的实例请求对象
export const create= ()=>{
	return new Fly();
}
const request = new Fly()

// 环境前缀添加
let baseURL = null;
if (process.env.NODE_ENV === 'development') {
	// 开发环境
	// baseURL = 'http://127.0.0.1:7001/api'
} else { 
	// 生产环境
	// baseURL = 'https://movie.lzzzero.com/api'
}
request.config.baseURL = baseURL

// 错误弹框提示
const errorPrompt = (err) => {
	wx.showToast({
		title: err.message || 'fetch data error.',
		icon: 'none'
	})
}

request.interceptors.request.use((request) => {
	wx.showNavigationBarLoading()
	return request
})

request.interceptors.response.use((response, promise) => {
	wx.hideNavigationBarLoading()
	//  服务器正确响应拦截逻辑处理
	// if (!(response && response.data && response.data.res === 0)) {
	//   errorPrompt(response)
	// }
	return promise.resolve(response.data)
}, (err, promise) => {
	wx.hideNavigationBarLoading()
	errorPrompt(err)
	return promise.reject(err)
})

export default request
