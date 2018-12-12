## 一、使用HttpServletResponse和HttpServletRequest
```
@RequestMapping("/itemList2")
public void itmeList2(HttpServletRequest request, HttpServletResponse response) throws Exception {
    // 查询商品列表
    List<Items> itemList = itemService.getItemList();
    // 向页面传递参数
    request.setAttribute("itemList", itemList);
    // 如果使用原始的方式做页面跳转，必须给的是jsp的完整路径
    request.getRequestDispatcher("/WEB-INF/jsp/itemList.jsp").forward(request, response);
}
```
通过response实现页面重定向：
```
response.sendRedirect("url")
```
## 二、使用ModelAndView
```
@RequestMapping("/index")
	public ModelAndView getIndex() {
		Map<String, List<Article>> map = articleService.getIndexData();
		System.out.println(map);
		ModelAndView modeAndView = new ModelAndView();
		modeAndView.addObject("map", map);
		modeAndView.setViewName("index");
		return modeAndView;
	}
```
## 三、Model/ModelMap
```
@RequestMapping("/itemEdit")
	publicStringitemEdit(HttpServletRequestrequest, Model model) {
		//从Request中取id
		String strId = request.getParameter("id");
		Integer id = null;
		//如果id有值则转换成int类型
		if (strId != null&& !"".equals(strId)) {
			id = newInteger(strId);
		} else {
			//出错
			returnnull;
		}
		Items items = itemService.getItemById(id);
		model.addAttribute("item", items);
		return"editItem";
	}
```
Redirect重定向
```
return "redirect:/item/itemList.action";
```
forward转发
```
return "forward:/item/itemList.action";
```
