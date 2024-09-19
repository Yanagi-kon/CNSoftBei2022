
// 生成随机数的方法 
export const generateRandom = () => {
	//用于生成uuid
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
	
	function guid() {
		return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
	}
	return new Promise((reslove,reject)=>{
	 let raid = 'uuid-'+guid();
		reslove(raid);
	})
} 
/**
 * 函数节流
 * @param {*} fn 
 * @param {*} interval 
 */
export const throttle = (fn, interval = 300) => {
  let canRun = true;
  return function () {
    if (!canRun) return;
    canRun = false;
    setTimeout(() => {
      fn.apply(this, arguments);
      canRun = true;
    }, interval);
  };
}
/**
 * 函数防抖 (只执行最后一次点击)
 * @param fn
 * @param delay
 * @returns {Function}
 * @constructor
 */
export const debounce = (fn, t) => {
  let delay = t || 500;
  let timer;
  console.log(fn)
  console.log(typeof fn)
  return function () {
    let args = arguments;
    if (timer) {
      clearTimeout(timer);
    }
    timer = setTimeout(() => {
      timer = null;
      fn.apply(this, args);
    }, delay);
  }
};