import provinces from './city-data/province.js';
import citys from './city-data/city.js';
import areas from './city-data/area.js';
export const getAreaCodeArr = (province, city, area) => {
	let resultArr=[]
	if (province) {
		let provinceResult = provinces.find(item => item.label == province)
		if(provinceResult){
			resultArr.push(provinceResult.value)
		}
		console.log(provinceResult)
	}
	if (city) {
		let cityResult;
		citys.map(item => {
			let result = item.find(it => it.label == city);
			if (result) {
				cityResult = result;
			}
		})
		if(cityResult){
			resultArr.push(cityResult.value)
		}
		console.log(cityResult)
	}
	if (area) {
		let areaResult;
		areas.map(item => {
			item.map(sItem => {
				let result = sItem.find(it => it.label == area);
				if (result) {
					areaResult = result;
				}
			})
		})
		if(areaResult){
			resultArr.push(areaResult.value)
		}
		console.log(areaResult)
	}
	return resultArr;
}
