package com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.filter

import com.multicampus.gamesungcoding.a11ymarketserver.common.jwt.provider.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(this::class.java)

    // @Throws(ServletException::class, IOException::class) // from Java, but not needed in Kotlin
    override fun doFilterInternal(
        req: HttpServletRequest,
        resp: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolveToken(req)

        if (!token.isNullOrBlank() && jwtTokenProvider.validateToken(token)) {
            val authentication = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication

            logger.debug("Saved Authentication to Security Context: ${authentication.name}")
        }

        filterChain.doFilter(req, resp)
    }

    private fun resolveToken(req: HttpServletRequest): String? {
        return req.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)
    }
}