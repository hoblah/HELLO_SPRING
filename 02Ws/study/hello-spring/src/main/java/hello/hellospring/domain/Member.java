package hello.hellospring.domain;
import javax.persistence.*;

@Entity
public class Member {

    /* import javax.persistence.Entity;의
     @Entity 어노테이션을 사용한다.
	 해당 어노테이션은 JPA가 관리하는 엔티티라고 표현한다.
	 PK를 매핑해줘야한다.
     pk는 등록시 디비에서 id값을 자동으로 생성해주는것을 아이덴티티라고 한다. */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 디비에 있는 컬럼명인 username이랑 매핑되는 것이다.
		 어노테이션을 사용하여 디비랑 매핑을 하는 것이다.
		 이 정보로 쿼리문을 만들수 있는 것이다.
		 JPA가 그렇게 동작 하는 것이다.
		 import javax.persistence.*; 임폴트. */
    //@Column(name = "username")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
