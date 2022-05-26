package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemerRepository implements MemberRepository{

    /* JPA는 EntityManager 로 모든 동작을 한다.
       그레이들에서 data-jpa를 받으면 스프링부트가 자동으로 EntityManager를 생성해준다.
       현재 데이터베이스와도 연결해준다. 이 만들어 진것을 인젝션 받으면 된다.
       applicaton.proprerties에서 jpa설정 했던것과
       데이터베이스 커넥션 정보랑 다 합쳐서 EntityManager라는것을 만들어준다.
       내부적으로 데이터소스를 다 들고 있어서 디비랑 통신하는것을 처리해준다.
       결론 - JPA를 사용하려면 EntityManager 를 주입 받아야 한다.
       주의해야할것은 JPA를 사용하려면 항상 트랜잭션이 있어야 한다. > Service에 어노테이션 추가.*/
    private final EntityManager em;

    @Autowired
    public JpaMemerRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        /* persist()메서드를 사용하면 영속화, 영구 저장하다 라는 뜻.
           하위코드를 세팅하면 JPA가 insert쿼리 다 만들어서 디비에 집어넣고,
           id 까지 member에다가 setId까지 다 해준다. */
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        /* find()를 사용하면 조회할 타입이랑 식별자 pk값 넣어주면 조회가된다. select문을 안하는것이다. */
        Member member = em.find(Member.class, id);
        // 값이 없을 수도 있기 때문에 옵셔널을 사용한다.
        return Optional.ofNullable(member);
    }

    /* findByName(), findAll() 메서드는 JPQL 이라는 객체지향 쿼리 언어를 써야하고
       거의 sql과 같고 차이는 인라인베리어블 사용하면 합쳐진다.
       우리는 테이블에 대하여 쿼리를 날리지만,
       JPA는 객체를 대상으로 쿼리를 날리면 sql로 번역이 된다.
       정확히 엔티티 대상으로 날리고 지금은 Member가 그 엔티티 이다.
       Member 엔티티를 조회하고 member, 즉 객체 자체를 셀렉트 하는것이다.
       이렇게 조회하면 끝나는 것이다. 기본 crud의 쿼리는 짜지 않아도되지만,
       List를 다루는 한건의 데이터가 아닌 pk기반이 아닌
       나머지 메서드 들은 JPQL 이라는것을 작성해 줘야 한다.
       JPA기술을 스프링에서 한번 깜싸서 제공하는 기술이 스프링 데이터 JAP 이다.
       그래서 data-jpa 라이브러리를 받은건데, 그것을 사용하면
       해당 메서드 들도 JPQL도 안짜도 된다.
       */
    @Override
    public Optional<Member> findByName(String name) {
        // 조회하면 결과가 나오고
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

        // 하나의 결과만 찾을거니까 하위코드로 반환 한다.
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member as m", Member.class)
                .getResultList();
    }
}
