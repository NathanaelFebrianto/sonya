package org.sonya.user.webapp.action;

import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.sonya.Constants;
import org.sonya.user.model.User;
import org.sonya.user.service.UserExistsException;
import org.sonya.user.service.UserManager;
import org.sonya.webapp.action.BasePage;
import org.springframework.mail.MailException;

/**
 * JSF Page class to handle signing up a new user.
 *
 * @author mraible
 */
public class SignupForm extends BasePage implements Serializable {
    private static final long serialVersionUID = 3524937486662786265L;
    private User user = new User();
    private UserManager userManager;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public String save() throws Exception {
        user.setEnabled(true);

        try {
            user = userManager.saveUser(user);
        } catch (AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity 
            log.warn(ade.getMessage());
            getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            return null; 
        } catch (UserExistsException e) {
            addMessage("errors.existing.user", new Object[]{user.getUsername(), user.getEmail()});

            // redisplay the unencrypted passwords
            user.setPassword(user.getConfirmPassword());
            return null;
        }

        addMessage("user.registered");
        getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // log user in automatically
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getConfirmPassword(), user.getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "mainMenu";
    }
}
