// package com.lincopilot.springcopilotonl.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.ProviderManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
// import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
// import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
// import org.springframework.security.web.context.SecurityContextRepository;
// import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
// import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

// import java.util.Collections;
// import java.util.Map;

// import com.lincopilot.springcopilotonl.csm.dao.LoginDao;

// @Configuration
// public class SecurityConfig {

//     @Bean
//     UserDetailsService userDetailsService(LoginDao loginDao) {
//         return username -> {
//             Map<String, Object> user = loginDao.selectUser(Collections.singletonMap("username", username));
//             if (user == null || !isEnabled(user.get("enabled"))) {
//                 throw new UsernameNotFoundException("User not found or disabled");
//             }

//             Object passwordHash = user.get("passwordHash");
//             Object dbUsername = user.get("username");
//             if (!(passwordHash instanceof String hash) || !(dbUsername instanceof String name)) {
//                 throw new UsernameNotFoundException("User record is invalid");
//             }

//             return org.springframework.security.core.userdetails.User.withUsername(name)
//                     .password(hash)
//                     .roles("USER")
//                     .build();
//         };
//     }

//     @Bean
//     PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
//             PasswordEncoder passwordEncoder) {
//         DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
//         provider.setPasswordEncoder(passwordEncoder);
//         return provider;
//     }

//     @Bean
//     AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
//         return new ProviderManager(authenticationProvider);
//     }

//     @Bean
//     SecurityContextRepository securityContextRepository() {
//         return new HttpSessionSecurityContextRepository();
//     }

//     @Bean
//     SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//         return new ChangeSessionIdAuthenticationStrategy();
//     }

//     @Bean
//     SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider,
//             SecurityContextRepository securityContextRepository) throws Exception {
//         http
//                 .csrf(csrf -> csrf
//                         .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                         .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
//                 .securityContext(context -> context.securityContextRepository(securityContextRepository))
//                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//                 .authenticationProvider(authenticationProvider)
//                 .authorizeHttpRequests(authorize -> authorize
//                         .requestMatchers(HttpMethod.GET, "/auth/csrf").permitAll()
//                         .requestMatchers(HttpMethod.POST, "/auth/actionLogin").permitAll()
//                         .anyRequest().authenticated())
//                 .formLogin(AbstractHttpConfigurer::disable)
//                 .httpBasic(AbstractHttpConfigurer::disable)
//                 .exceptionHandling(exceptions -> exceptions
//                         .authenticationEntryPoint((request, response, exception) -> response.sendError(401)));

//         return http.build();
//     }

//     private static boolean isEnabled(Object enabled) {
//         return enabled instanceof Number number && number.intValue() == 1
//                 || enabled instanceof Boolean value && value;
//     }
// }
