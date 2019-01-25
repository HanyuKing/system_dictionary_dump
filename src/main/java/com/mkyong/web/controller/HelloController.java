package com.mkyong.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;

@Controller
public class HelloController {

	private volatile boolean stop = false;

	@RequestMapping(value = "stop", method = RequestMethod.GET)
	@ResponseBody
	public String stop() {
		stop = true;
		return "success";
	}

	@RequestMapping(value = "classloader", method = RequestMethod.GET)
	@ResponseBody
	public String classloader() {
		while (!stop) {
			try {
				Class.forName("java.lang.Integer", false, new MyClassLoader());
				Class.forName("java.lang.Number", false, new MyClassLoader());
				Class.forName("java.lang.Object", false, new MyClassLoader());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return "success";
	}

	class MyClassLoader extends ClassLoader {
		@Override
		protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			return super.loadClass(name, resolve);
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(ModelMap model, HttpServletRequest req) throws InterruptedException {
//		byte[] M_4 = new byte[1024 * 1024 * 4];
//		while (true) {
//			Thread.sleep(100);
//			M_4 = new byte[1024 * 1024 * 4];
//		}
//		//model.addAttribute("message", "Spring 3 MVC Hello World");
		return "hello";

	}

	@RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name, HttpServletRequest req) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("run........");
				new SystemDictionaryDump().dump(new String[] {ManagementFactory.getRuntimeMXBean().getName().split("@")[0], ";"});
			}
		}).start();

		String path = req.getServletContext().getRealPath("/");

		ModelAndView model = new ModelAndView();
		model.setViewName("hello");
		model.addObject("msg", name);

		return model;

	}

}