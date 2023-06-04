package com.tistory.eclipse4j.auth.user.entity

import com.tistory.eclipse4j.auth.token.entity.TokenEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(
    name = "security_user",
    indexes = [
        // ,Index(name = "idx_dic_multiple_columns", columnList = "word, xxxx")
    ]
)
@Comment("메모")
class UserEntity(
    firstname: String? = null,
    lastname: String? = null,
    email: String? = null,
    pwd: String? = null,
    role: Role? = null,
) : UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null

    @Column(name = "firstname")
    var firstname: String? = firstname
        private set

    @Column(name = "lastname")
    var lastname: String? = lastname
        private set

    @Column(name = "email")
    var email: String? = email
        private set

    @Column(name = "pwd")
    var pwd: String? = pwd
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    var role: Role? = role
        private set

    @OneToMany(mappedBy = "user")
    var tokens: MutableList<TokenEntity> = mutableListOf()

    override fun getAuthorities(): MutableList<SimpleGrantedAuthority> {
        return role?.getAuthorities() ?: mutableListOf()
    }

    override fun getPassword(): String {
        return pwd!!
    }

    override fun getUsername(): String {
        return email!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}