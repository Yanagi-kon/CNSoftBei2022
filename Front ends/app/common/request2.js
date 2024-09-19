import qs from 'qs';
	export default {

		// 全局配置
		common: {
			// baseUrl: "http://5b3427d739.zicp.vip:55917/",
			baseUrl: "http://36.138.201.130:9999/",
			header: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			data: {},
			method: 'GET',
		},
		// 请求 返回promise
		request(options = {}) {
			let common = this.common;
			// 组织参数
			options.url = common.baseUrl + options.url;
			options.header = options.header || common.header;
			options.data = options.data || common.data;
			options.method = options.method || common.method;
			options.dataType = options.dataType || common.dataType;

			// 请求
			return new Promise((res, rej) => {
				uni.request({
					...options,
					success(result) {
						let {
							statusCode,
							data
						} = result;
						// 返回原始数据
						if (options.native) return res(result);

						// 服务端失败
						if (statusCode !== 200) {
							if (options.toast !== false) {
								uni.showToast({
									title: data.msg || '服务端失败',
									icon: 'none'
								});
							}
							return rej(data)
						}

						// 成功
						res(data)
					},
					fail({
						errMsg
					}) {
						uni.showToast({
							title: errMsg || '请求失败',
							icon: 'none'
						});
						return rej()
					}
				});
			})
		},
		// get请求
		get(url, data = {}, options = {}) {
			options.url = url;
			options.data = data;
			options.method = 'GET';
			return this.request(options)
		},
		// post请求
		post(url, data = {}, options = {}) {
			options.url = url;
			options.data = qs.stringify(data);
			options.method = 'POST';
			return this.request(options)
		},
		// delete请求
		del(url, data = {}, options = {}) {
			options.url = url;
			options.data = data;
			options.method = 'DELETE';
			return this.request(options)
		}
	}
