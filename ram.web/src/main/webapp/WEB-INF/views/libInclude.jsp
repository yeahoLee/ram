<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<script src="resources/ace/js/jquery-1.11.3.min.js"></script>

<link rel="stylesheet" type="text/css" href="resources/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="resources/bootstrap/css/bootstrap-theme.min.css">
<script type="text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>

<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->
<!--[if lte IE 8]>
<script src="resources/bootstrap/js/html5shiv.min.js"></script>
<script src="resources/bootstrap/js/respond.min.js"></script>
<![endif]-->

<link rel="stylesheet" type="text/css" href="resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="resources/easyui/themes/icon.css">
<script type="text/javascript" src="resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="resources/easyui/locale/easyui-lang-zh_CN.js"></script>

<!-- <link rel="stylesheet" href="resources/fontawesome/css/all.css" /> -->
<link rel="stylesheet" href="resources/fontawesome/css/font-awesome.min.css"/>

<script type="text/javascript" src="resources/js/common.js"></script>
<script>

	/**
	 * combobox和combotree模糊查询
	 */
	(function(){
		//combobox可编辑，自定义模糊查询
		$.fn.combobox.defaults.editable = true;
		$.fn.combobox.defaults.filter = function(q, row){
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q) >= 0;
		};
		//combotree可编辑，自定义模糊查询
		$.fn.combotree.defaults.editable = true;
		$.extend($.fn.combotree.defaults.keyHandler,{
			up:function(){
				console.log('up');
			},
			down:function(){
				console.log('down');
			},
			enter:function(){
				console.log('enter');
			},
			query:function(q){
				var t = $(this).combotree('tree');
				var nodes = t.tree('getChildren');
				for(var i=0; i<nodes.length; i++){
					var node = nodes[i];
					if (node.text.indexOf(q) >= 0){
						$(node.target).show();
					} else {
						$(node.target).hide();
					}
				}
				var opts = $(this).combotree('options');
				if (!opts.hasSetEvents){
					opts.hasSetEvents = true;
					var onShowPanel = opts.onShowPanel;
					opts.onShowPanel = function(){
						var nodes = t.tree('getChildren');
						for(var i=0; i<nodes.length; i++){
							$(nodes[i].target).show();
						}
						onShowPanel.call(this);
					};
					$(this).combo('options').onShowPanel = opts.onShowPanel;
				}
			}
		});
	})(jQuery);

</script>
