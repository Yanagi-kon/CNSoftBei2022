<template>
  <el-row style="margin: 20px;">
    <el-button type="primary" style="margin: 5px;" @click="addUserClick">添加管理员信息</el-button>
    <el-table
      :data="userInfo.content"
      style="width: 100%;margin-top: 20px;overflow-x: auto;">

      <el-table-column
        prop="number"
        label="管理员编号">
      </el-table-column>
      <el-table-column
        prop="name"
        label="姓名">
      </el-table-column>

  <!--    <el-table-column
        prop="address"
        label="地区">
      </el-table-column> -->
      <el-table-column
        prop="phone"
        label="手机号">
      </el-table-column>

      <el-table-column
        prop="email"
        label="邮箱"
        width="200">
      </el-table-column>

<!--      <el-table-column-->
<!--        prop="bankNumber"-->
<!--        label="地区"-->
<!--        idth="200">-->
<!--      </el-table-column>-->

      <el-table-column
        prop="sex"
        label="性别">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.sex === 1 ? 'primary' : 'success'"
            disable-transitions>{{scope.row.sex===1?'男':'女'}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="department.name"
        label="责任部门">
      </el-table-column>

      <el-table-column
        prop="role"
        label="角色">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.role === 1 ? 'primary' : 'success'"
            disable-transitions>{{scope.row.role===1?'总管':'普通管理员'}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column
        label="注册时间"
        prop="createTime"
        idth="200">
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.createTime|formatDate}}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="editUser(scope.$index, scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="delUser(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      layout="prev, pager, next"
      :page-size="10"
      :total="userInfo.totalElements"
      :page-count="userInfo.totalPage"
      @current-change="changeUserPage">
    </el-pagination>

    <!--添加或者修改用户-->
    <el-dialog :title="userTitle" :visible.sync="showUser" @close="closeDialog">
      <el-form :model="user" ref="userForm">
     <!--   <el-form-item v-if="!isAddUser" label="id" :label-width="formLabelWidth">
          <el-input v-model="user.id" autocomplete="off" disabled></el-input>
        </el-form-item>-->
        <el-form-item v-if="!isAddUser" label="管理员编号" :label-width="formLabelWidth">
          <el-input v-model="user.number" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" :label-width="formLabelWidth">
          <el-input v-model="user.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item v-if="isAddUser" label="密码" :label-width="formLabelWidth">
          <el-input v-model="user.password" autocomplete="off"></el-input>
             </el-form-item>
        <!--  <el-form-item label="居住地址" :label-width="formLabelWidth">
            <el-input v-model="user.address" autocomplete="off"></el-input>
          </el-form-item>-->
             <el-form-item label="手机号" :label-width="formLabelWidth">
               <el-input v-model="user.phone" autocomplete="off"></el-input>
             </el-form-item>
             <el-form-item label="邮箱" :label-width="formLabelWidth">
               <el-input v-model="user.email" autocomplete="off"></el-input>
             </el-form-item>
     <!--        <el-form-item label="银行卡号" :label-width="formLabelWidth">-->
<!--          <el-input v-model="user.bankNumber" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="性别" prop="sex" :label-width="formLabelWidth">
          <el-col :span="8">
            <el-radio-group v-model="user.sex">
              <el-radio-button label="1">男</el-radio-button>
              <el-radio-button label="2">女</el-radio-button>
            </el-radio-group>
          </el-col>
        </el-form-item>

        <el-form-item label="部门名称" :label-width="formLabelWidth">
          <el-select v-model="user.department.id" placeholder="部门名称">
            <el-option v-for="(item,index) in departmentNames" :key="index" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="角色" prop="role" :label-width="formLabelWidth">
          <el-col :span="8">
            <el-radio-group v-model="user.role">
              <el-radio-button label="1">总管</el-radio-button>
              <el-radio-button label="2">普通管理员</el-radio-button>
            </el-radio-group>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showUser = false">取 消</el-button>
        <el-button v-if="isAddUser" type="primary" @click="addUser">添加</el-button>
        <el-button v-else type="primary" @click="updateUser">修改</el-button>
      </div>
    </el-dialog>
  </el-row>
</template>

<script>
import { getUserPages, addUser, updateUser2, delSysUser } from '@/api/user'
  import {getDepartmentNames} from '@/api/department';
  import {parseTime} from "@/utils";
  export default {
    name: 'Admin',
    data(){
      return{
        activeName:"account",
        formLabelWidth: "150px",
        userInfo:{
          content:[],//数据
          currentPage:0,//当前页数
          totalElements:0,//总条数
          totalPage:0//总页数
        },
        //信息
        user:{
          id:null,
          name:"",
          note:"",
          money:null,
          startTime:"",
          endTime:"",
          department:{
            id:null,
            name:""
          }
        },
        showUser:false,
        isAddUser:true,
        userTitle:"添加信息",
        pageSize:10,
        departmentNames:[]
      };
    },
    mounted(){
      this.getUserPages(this.userInfo.currentPage);
      this.getDepartmentNames();
    },
    methods:{

      getUserPages(page){
        getUserPages(page,this.pageSize).then(response => {
          const { data} = response
          this.userInfo = data;
          console.log(data)
        }).catch(error => {
          console.log(error)
        });
      },
      //用户分页
      changeUserPage(page){
        console.log("切换页码"+page)
        this.getUserPages(page-1);
      },
      addUserClick(){
        this.showUser = true;
        this.userTitle="添加用户信息";
        this.isAddUser = true;
        this.user={
          id:null,
            name:"",
            note:"",
            money:null,
            startTime:"",
            endTime:"",
            department:{
            id:null,
              name:""
          }
        }
      },
      //添加用户信息
      addUser(){
        addUser(this.user).then(response => {
          this.$message({
            message: "添加用户成功",
            type: "success"
          });
          if (this.$refs["userForm"] !== undefined) {
            this.$refs["userForm"].resetFields();
          }
          this.showUser = false;
          this.getUserPages(this.userInfo.currentPage);
        }).catch(error => {
          console.log(error)
        });
      },
      //更新用户信息
      editUser(index,row){
        console.log(row);
        this.user = row;
        this.showUser = true;
        this.showUser = true;
        this.isAddUser = false
        this.userTitle="修改管理员信息";
      },
      //修改用户信息
      updateUser(){
        updateUser2(this.user).then(response => {
          this.$message({
            message: "修改管理员成功",
            type: "success"
          });
          if (this.$refs["userForm"] !== undefined) {
            this.$refs["userForm"].resetFields();
          }
          this.showUser = false;
          this.getUserPages(this.userInfo.currentPage);
        }).catch(error => {
          console.log(error)
        });
      },
      delUser(index,row){
        delSysUser(row.id).then(response => {
          this.$message({
            message: "删除用户成功",
            type: "success"
          });
          this.getUserPages(0);
        }).catch(error => {
          console.log(error)
        });
      },
      getDepartmentNames(){
        getDepartmentNames().then(response => {
          this.departmentNames = response.data;
        }).catch(error => {
          console.log(error)
        });
      },
      closeDialog(){
        this.user={
          id:null,
          name:"",
          note:"",
          money:null,
          startTime:"",
          endTime:"",
          department:{
            id:null,
            name:""
          }
        }
      }
    },
    filters:{
      formatDate:function(time) {
        if(time!=null&&time!=="")
        {
          // var date = new Date(time);
          return parseTime(time, '{y}-{m}-{d}');
        }else{
          return "";
        }
      }
    }
  }
</script>

<style scoped>

</style>
