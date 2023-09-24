package com.grgr.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grgr.dto.OrderPage;
import com.grgr.dto.ProductCartDTO;
import com.grgr.exception.CartDeleteFailException;
import com.grgr.exception.CartNullException;
import com.grgr.exception.OrderInsertFailException;
import com.grgr.service.OrderPageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderPageController {
	private final OrderPageService orderPageService;
	
	// 주문테이블에 저장
	@PostMapping("/add")
	public ResponseEntity<String> addOrderItems(@RequestParam("selectedItemList[]") List<Integer> selectedItemList, @RequestParam int totalPrice, HttpSession session ) throws CartNullException, CartDeleteFailException, OrderInsertFailException {
		
		session.setAttribute("totalPrice", totalPrice);
		
		Integer loginUno =(Integer)session.getAttribute("loginUno");
		
		//주문테이블에 저장 및 장바구니에서 삭제
		orderPageService.addOrderedItems(selectedItemList, loginUno);
		
		
		return new ResponseEntity<String> ("success", HttpStatus.OK);
	}


	// 장바구니
	@RequestMapping("/cart/{productCartNo}")
	public String cartOrderPage(@RequestBody int productCartNo,
			HttpSession session, Model model) {
		log.info("@@@@@ OrderPageController 클래스의 cartOrderPage 호출");

		Integer loginUno = (Integer) session.getAttribute("loginUno");
		log.info("loginUno"+loginUno);

		Map<String, Object> result = orderPageService.getCartOrderPage(loginUno, productCartNo);
		log.info("result"+result);
		
		model.addAttribute("cartOrderPage", result.get("cartOrderPage"));

		return "board/orderpage";
	}

	// 바로구매
	@RequestMapping("/product/{productId}")
	public String porductOrderPage(@PathVariable Integer productId, HttpSession session, Model model) {
		log.info("@@@@@ OrderPageController 클래스의 porductOrderPage 호출");

		Integer loginUno = (Integer) session.getAttribute("loginUno");

		Map<String, Object> result = orderPageService.getProductOrderPage(loginUno, productId);

		model.addAttribute("product", result.get("product"));

		return "board/orderpage";
	}


}