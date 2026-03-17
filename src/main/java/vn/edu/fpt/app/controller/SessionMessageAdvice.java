package vn.edu.fpt.app.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@ControllerAdvice
public class SessionMessageAdvice {

    @ModelAttribute("flashMessage")
    public void exposeFlashMessage(Map<String, Object> model, HttpSession session) {
        Object message = session.getAttribute("message");
        Object messageType = session.getAttribute("messageType");

        if (message != null) {
            model.put("message", message.toString());
            model.put("messageType", messageType != null ? messageType.toString() : "info");
            session.removeAttribute("message");
            session.removeAttribute("messageType");
        }
    }
}

