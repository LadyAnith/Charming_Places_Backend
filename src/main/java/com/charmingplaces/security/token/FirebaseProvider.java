package com.example.apirestfirebase.security.token;

import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.apirestfirebase.security.model.Token;
import com.example.apirestfirebase.security.model.User;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

@Component
public class FirebaseProvider implements AuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(FirebaseProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Token token = (Token) authentication;
		try {
			FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token.getToken(), true);
			String uid = firebaseToken.getUid();
			UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
			return new User(userRecord);

		} catch (FirebaseAuthException e) {
			logger.error("Fail {}", getErrorMessage(e.getAuthErrorCode()));
		}

		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(Token.class);
	}


	public String getErrorMessage(AuthErrorCode authErrorCode) {
		
		Map<AuthErrorCode, String> errorCodes = new EnumMap<>(AuthErrorCode.class);
		errorCodes.put(AuthErrorCode.CERTIFICATE_FETCH_FAILED, "Failed to retrieve public key certificates required to verify JWTs.");
		errorCodes.put(AuthErrorCode.CONFIGURATION_NOT_FOUND, "No IdP configuration found for the given identifier.");
		errorCodes.put(AuthErrorCode.EMAIL_ALREADY_EXISTS, "A user already exists with the provided email.");
		errorCodes.put(AuthErrorCode.EMAIL_NOT_FOUND, "No user record found for the given email, typically raised when generating a password reset link using an email for a user that is");
		errorCodes.put(AuthErrorCode.EXPIRED_ID_TOKEN, "The specified ID token is expired.");
		errorCodes.put(AuthErrorCode.EXPIRED_SESSION_COOKIE, "The specified session cookie is expired.");
		errorCodes.put(AuthErrorCode.INVALID_DYNAMIC_LINK_DOMAIN, "The provided dynamic link domain is not configured or authorized for the current project.");
		errorCodes.put(AuthErrorCode.INVALID_ID_TOKEN, "The specified ID token is invalid.");
		errorCodes.put(AuthErrorCode.INVALID_SESSION_COOKIE, "The specified session cookie is invalid.");		
		errorCodes.put(AuthErrorCode.PHONE_NUMBER_ALREADY_EXISTS, "A user already exists with the provided phone number.");
		errorCodes.put(AuthErrorCode.REVOKED_ID_TOKEN, "The specified ID token has been revoked.");
		errorCodes.put(AuthErrorCode.REVOKED_SESSION_COOKIE, "The specified session cookie has been revoked.");
		errorCodes.put(AuthErrorCode.TENANT_ID_MISMATCH, "Tenant ID in the JWT does not match.");
		errorCodes.put(AuthErrorCode.TENANT_NOT_FOUND, "No tenant found for the given identifier.");
		errorCodes.put(AuthErrorCode.UID_ALREADY_EXISTS, "A user already exists with the provided UID.");
		errorCodes.put(AuthErrorCode.UNAUTHORIZED_CONTINUE_URL, "The domain of the continue URL is not whitelisted. Whitelist the domain in the Firebase console.");		errorCodes.put(AuthErrorCode.USER_DISABLED, "The user record is disabled.");
		errorCodes.put(AuthErrorCode.USER_NOT_FOUND, "No user record found for the given identifier.");
		
		return errorCodes.getOrDefault(authErrorCode, "Failed Authentication");
		
	}
}
