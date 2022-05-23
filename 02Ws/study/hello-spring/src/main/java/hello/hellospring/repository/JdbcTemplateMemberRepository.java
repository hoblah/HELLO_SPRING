package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public JdbcTemplateMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        /* SimpleJdbcInsert 은 jdbcTemplate을 매개변수로 넘겨서 만드는데
           jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id"); 를
           사용하면 쿼리를 짤 필요가 없다.
           테이블명이랑 name이 있으면 insert문을 만들수 있어서
           SimpleJdbcInsert 가 insert문을 만들어준다.
        */
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());
        Number key = jdbcInsert.executeAndReturnKey(new
                MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }
    
    @Override
    public Optional<Member> findById(Long id) {
        // List<Member> result변수에 받으면 리스트가 나온다.
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRewMapper());
        // 저장한 리스트를 stream으로 바꿔서 findAny() 하여 옵셔널로 반환함.
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        return null;
    }

    // Replace with lambda 선택하면 자동으로 람다식으로 변함.
    private RowMapper<Member> memberRewMapper(){
        return (rs, rowNum) -> {

            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }

/* Replace with lambda 선택하면 자동으로 람다식으로 변함.
    private RowMapper<Member> memberRewMapper(){
        return new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {

                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return member;
            }
        };
    }
*/

}
