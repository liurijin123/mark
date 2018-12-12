## 一、使用HttpServletRequest和 Session
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
## 三、
## 四、
