package com.grazac.springauthgrazac.orders;

import com.grazac.springauthgrazac.config.RabbitConfig;
import com.grazac.springauthgrazac.user.User;
import com.grazac.springauthgrazac.user.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.grazac.springauthgrazac.user.Role.ROLE_USER;

@Component
public class OrderConsumer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OrderConsumer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void consumeMessage(@Payload ProducerMessage request) {
        System.out.println("Received: " + request);
        // {
        //"message":"ab@g.com"
        //}
        createNewUser(request.getEmail());
    }

    private void createNewUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) return;

        String password = passwordEncoder.encode("password");
        User userInstance = User.builder().email(email).isVerified(true)
                .username(email)
                .password(password)
                .name(email)
                .role(ROLE_USER).build();
        userRepository.save(userInstance);
        System.out.println("====================================");
        System.out.println("User created: " + email);
        System.out.println("====================================");
    }
}