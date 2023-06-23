package fr.utc.sr03.chat.config;

import org.hibernate.Session;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * Avant l'exécution de la méthode de Controller
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        // Logique de vérification de connexion
        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute("email");
        if (loginUser != null) {
            return true;
        }
        // Si l'utilisateur n'est pas connecté, redirige vers la page de connexion
        response.sendRedirect("/login");
        return false;
    }
}
