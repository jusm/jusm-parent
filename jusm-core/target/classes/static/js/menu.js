var Menu = {
    id: "menuTable",
    table: null,
    layerIndex: -1,
    init:function(){
    	$("#submit").click(function(){
    		Menu.save();
    	});
    },
    reload: function () {
    	Menu.table.refresh();
    	$("#detail").hide();
      	$("#table").show();
    },
    newAdd:function(){
    	$("#table").hide();
      	$("#detail").show();
    },
    update:function(){
    	$("#table").hide();
      	$("#detail").show();
    },
    save:function(){
	    if(Menu.validator()){
	       return;
	    }

    	alert('操作成功', function(){
    		 Menu.reload();
        });
    },
    validator: function () {
    	var name = $("input[name='name']").val();
    	var parentId = $("input[name='parent.id']").val();
    	var parentName = $("input[name='parent.name']").val();
    	var url = $("input[name='url']").val();
    	var permission = $("input[name='permission']").val();
    	var order = $("input[name='order']").val();
    	var icon = $("input[name='icon']").val();
    	
        if(isBlank(name)){
            alert("菜单名称不能为空");
            return true;
        }
        //菜单
        if(vm.menu.type === 1 && isBlank(vm.menu.url)){
            alert("菜单URL不能为空");
            return true;
        }
    }
};

Menu.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
//        {title: '主键标识', field: 'id', width: '110px'},
        {title: '图标', field: 'icon', width: 'auto', align: 'center',  formatter: function(item, index){ return item.icon == null ? '' : '<i class="'+item.icon+' fa-lg"></i>';}},
        {title: '菜单名称', field: 'name',  width: 'auto'},
        {title: '菜单编码', field: 'number', visible: false, width: 'auto'},
        {title: '排序号', field: 'position', sortable: true, width: 'auto',align: 'right'},
        {title: '菜单URI', field: 'uri', sortable: true, width: 'auto'},
        {title: '类型', field: 'type', sortable: true, width: 'auto'},
        {title: '描述', field: 'description',sortable: true},
		{title: '操作', width: 'auto', align: 'center',  formatter: function(item, index){
			var opt = item.number.split("_").length < 4 ? '<a class="btn btn-sm btn-success" th:href="#"><i class="fa fa-plus">&nbsp新增</i></a>&nbsp;':'';
			return opt + '<a class="btn btn-sm btn-primary" th:href="#">编辑</a>&nbsp;'+
			'<button th:attr="del_uri=#" class="btn btn-sm btn-danger deleteBtn">删除</button>';
		}}]
    return columns;
};

$(function () {
    var colunms = Menu.initColumn();
    var table = new TreeTable(Menu.id, "menulist", colunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("parentId");
    table.setExpandAll(false);
    table.setHeight("auto");
    table.init();
    Menu.table = table;
    Menu.init();
});


var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parent.id",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;
function getMenuId () {
    var selected = $('#menuTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return false;
    } else {
        return selected[0].id;
    }
}

 