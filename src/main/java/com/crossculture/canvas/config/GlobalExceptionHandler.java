// package com.crossculture.canvas.config;

// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.servlet.ModelAndView;

// @ControllerAdvice
// public class GlobalExceptionHandler {

//     @ExceptionHandler(Exception.class)
//     public ModelAndView handleException(Exception ex) {
//         ModelAndView mav = new ModelAndView();
//         mav.addObject("errorMessage", ex.getMessage());
//         mav.addObject("exception", ex);
//         mav.setViewName("error");
//         return mav;
//     }
    
//     @ExceptionHandler(RuntimeException.class)
//     public ModelAndView handleRuntimeException(RuntimeException ex) {
//         ModelAndView mav = new ModelAndView();
//         mav.addObject("errorMessage", "An error occurred: " + ex.getMessage());
//         mav.addObject("exception", ex);
//         mav.setViewName("error");
//         return mav;
//     }
    
//     @ExceptionHandler({jakarta.validation.ConstraintViolationException.class, 
//                      org.springframework.web.bind.MethodArgumentNotValidException.class})
//     public ModelAndView handleValidationException(Exception ex) {
//         ModelAndView mav = new ModelAndView();
//         mav.addObject("errorMessage", "Validation error: " + ex.getMessage());
//         mav.addObject("exception", ex);
//         mav.setViewName("error");
//         return mav;
//     }
// }