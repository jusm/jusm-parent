package com.github.jusm.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 *  
 */
public class JwtTokenHandler implements Serializable {

	private static final long serialVersionUID = -3301605591108950415L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String CLAIM_KEY_USERNAME = "sub";
	private static final String CLAIM_KEY_CREATED = "created";
	
	private String tokenPrefix;
	
	private String tokenHeaderKey;

	private String secret;

	private Long expiration;
	
	/**
	 * 是否校验token这里方便测试定位问题
	 */
	private boolean validation;

	public JwtTokenHandler(String tokenPrefix, String tokenHeaderKey, String secret, Long expiration,boolean validation) {
		this.tokenPrefix = tokenPrefix;
		this.tokenHeaderKey = tokenHeaderKey;
		this.secret = secret;
		this.expiration = expiration;
		this.validation = validation;
	}

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	public Date getCreatedDateFromToken(String token) {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	private Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	public String generateToken(JwtUser userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		claims.put(CLAIM_KEY_CREATED, userDetails.getLastLoginTime());
		return generateToken(claims);
	}

	private String generateToken(Map<String, Object> claims) {
		return  Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getCreatedDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && !isTokenExpired(token);
	}

	public String refreshToken(String token, Date date) {
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, date);
			refreshedToken = generateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	public Boolean validateToken(String token, final JwtUser user) {
		final String username = getUsernameFromToken(token);
		final Date created = getCreatedDateFromToken(token);
		final Date expiration = getExpirationDateFromToken(token);
		final Date now = new Date();
		logger.info(" Validate token: " + token);

		boolean result = (user != null && user.getLastLoginTime() != null
				&& created.compareTo(user.getLastLoginTime()) == 0 && username.equals(user.getUsername())
				&& !isTokenExpired(token) && now.before(expiration)
				&& !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));

		if (!result) {
			logger.warn(" Validate token failed! ");
			logger.info(" user != null   >>>> " + (user != null));
			logger.info(" user.getLastLoginTime() != null   >>>> " + (user.getLastLoginTime() != null));
			logger.info(" created.compareTo(user.getLastLoginTime())==0   >>>> "
					+ (created.compareTo(user.getLastLoginTime()) == 0));
			logger.info(" created  >>>> " + created.getTime());
			logger.info(" user.getLastLoginTime()  >>>> " + user.getLastLoginTime().getTime());
			logger.info(" username.equals(user.getUsername())   >>>> " + user.getUsername() + (username.equals(user.getUsername())));
			logger.info(" !isTokenExpired(token)  >>>> " + (!isTokenExpired(token)));
//			logger.info(" now.after(created)  >>>> " + (now.after(created)));// 集群环境下 这个很难保证所以去掉
			logger.info(" now.before(expiration)  >>>> " + (now.before(expiration)));
			logger.info(" !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())  >>>> "
					+ (!isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())));
		}
		return result;
	}

	public String getTokenHeaderKey() {
		return tokenHeaderKey;
	}

	public Long getExpiration() {
		return expiration;
	}

	public String getTokenPrefix() {
		return tokenPrefix;
	}

	public boolean isValidation() {
		return validation;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}
}