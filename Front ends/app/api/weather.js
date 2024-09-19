import {create} from '@/utils/request.js'
const request = create();


/** 获取定位详情
 */
const getLocationDetail= (data)=>{
	let url =`https://apis.map.qq.com/ws/geocoder/v1`
	return request.get(url,data)
}
/** 获取天气
 * @param {Object} province 省
 * @param {Object} city  市
 * @param {Object} county 县
 */
const getWeather= (data)=>{
	let url =`https://wis.qq.com/weather/common`
	return request.get(url,data)
}

export default {
	getWeather,
	getLocationDetail
}