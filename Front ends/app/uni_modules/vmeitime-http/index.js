import http from './interface'
//设置请求前拦截器
http.interceptor.request = (config) => {
	// config.header = {
	// 	'token': store.state.userInfo.token,
	// }
	
}
//设置请求结束后拦截器
http.interceptor.response = (response) => {
	
	
}

export const getNewsPage= (data) => {

	return http.request({
		url: '/api/news/getNewsPage',
		method: 'POST',
		data,
	})
}
export const searchNews= (data) => {

	return http.request({
		url: '/api/news/searchNews',
		method: 'POST',
		data,
	})
}
export const recommend= (data) => {

	return http.request({
		url: '/api/news/recommend?uid='+data,
		method: 'POST',
		// data,
	})
}
export const userRegister= (data) => {

	return http.request({
		url: '/api/user/userRegister',
		method: 'POST',
		data,
	})
}
export const getNews= (data) => {

	return http.request({
		url: '/api/news/getNews',
		method: 'POST',
		data,
	})
}

export const GetNewsAudio= (data) => {

	return http.request({
		url: '/api/news/ttsNews',
		method: 'POST',
		data,
	})
}

export const getUser= (data) => {

	return http.request({
		url: '/api/user/getUser',
		method: 'POST',
		data,
	})
}

export const personalCenter= (data) => {

	return http.request({
		url: '/api/user/personalCenter',
		method: 'POST',
		data:data
	})
}

export const ttes= (data) => {

	return http.request({
		url: '/api/news/ttes',
		method: 'POST',
		data,
	})
}
export const history= (data) => {

	return http.request({
		url: '/api/backend/user/history',
		method: 'POST',
		data,
	})
}

export const ttsText= (data) => {

	return http.request({
		url: '/api/general/ttsText',
		method: 'POST',
		data,
	})
}

export const itemRec= (data) => {

	return http.request({
		url: '/api/general/itemRec',
		method: 'POST',
		data,
	})
}



export default {
	ttsText,
	itemRec,
	history,
	ttes,
	personalCenter,
	getUser,
	GetNewsAudio,
	getNews,
	userRegister,
	getNewsPage,
	searchNews,
	recommend,

}
