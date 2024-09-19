<template>
  <view class="pca-picker-cc">
    <picker mode="multiSelector" @columnchange="bindMultiPickerColumnChange" @change="change" :value="multiIndexArr" :range="multiArray">
      <slot></slot>
    </picker>
  </view>
</template>

<script>
let _self;
import jsonData from './pca-code.json'
export default {
  name: "pca-picker-cc",
  // 此处定义传入的数据
  props: {
    addressList: {
      type: Array,
      default: []
    }
  },
  computed: {
    multiIndexArr(){
      return [this.provinceIndex, this.cityIndex, this.countyIndex];
    },
    province() {
      return this.provinceList[this.provinceIndex] || {};
    },
    provinceList() {
      return this.data;
    },
    provinceNameList() {
      return this.provinceList.map((item, index) => item.name || '');
    },
    city() {
      return this.cityList[this.cityIndex] || {};
    },
    cityList() {
      return this.province.children || [];
    },
    cityNameList() {
      return this.cityList.map((item, index) => item.name || '');
    },
    county() {
      return this.countyList[this.countyIndex] || {};
    },
    countyList() {
      return this.city.children || [];
    },
    countyNameList() {
      return this.countyList.map((item, index) => item.name || '');
    },
    multiArray() {
      return [this.provinceNameList, this.cityNameList, this.countyNameList];
    },
    addressResultObj() {
      return {province: this.province, city: this.city, county: this.county};
    },
    addressResultList(){
      return Object.values(this.addressResultObj).map((item, index) => item.name || '');
    }
  },
  data() {
    return {
      data: [],
      provinceIndex: 0,
      cityIndex: 0,
      countyIndex: 0,
      keys: ['province', 'city', 'county']
    };
  },
  //在实例创建完成后被立即调用。
  mounted() {
    _self = this;
    this.loadData();
  },
  methods: {
    loadData() {
      _self.data = jsonData;
      _self.init();
    },
    init() {
      if (this.addressList.length === 3) {
        let key, index;
        for (let i in this.keys) {
          key = this.keys[i];
          index = this[`${key}NameList`].indexOf(this.addressList[i]);
          if (index <= -1) {
            index = 0;
          }
          this.$data[`${key}Index`] = index;
        }
        this.change();
      }
    },
    bindMultiPickerColumnChange: function (e) {
      let index = e.detail.column;
      let key = this.keys[index];
      this.$data[`${key}Index`] = e.detail.value;
      while (index < this.keys.length) {
        index++;
        key = this.keys[index];
        this.$data[`${key}Index`] = 0;
      }
    },
    change(e){
      // console.log(e)
      this.$emit('update:addressList', this.addressResultList);
      this.$emit('getAddressResultObj', this.addressResultObj);
    },
    getAddressResultObj(){
      return this.getAddressResultObj;
    }
  }
}
</script>

<style lang="scss">

</style>
