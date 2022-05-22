package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// MemberRepository 인터페이스를 implements 받아
// 구현체 작성하기.
public class JdbcMemberRepository implements MemberRepository {

    // 디비에 붙기위한 DataSouce 객체의 변수 생성. > import javax.sql.DataSource; 임포트필요.
    private final DataSource dataSource;

    /* 데이터베이스의 생성자 메서드를 통하여 스프링에서 주입받음.
       application.properties 파일에서 데이터베이스 세팅을 하면
       스프링에서는 DataSource의 접속정보를 만들어 놓는다.
       스프링을 통해서 데이터소스를 주입받게된다. */
    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        /* getConnection 메서드를 사용하여
           진짜 데이터베이스와 연결되는 소켓이 열린것을 얻을 수 있다.
           여기에 sql문을 날려서 디비에 처리하면 된다. > dataSource.getConnection();  */
    }

    @Override
    public Member save(Member member) {
        // 등록 sql 쿼리문 세팅.
        // ? 물음표는 파라미터 바인딩시 사용함. > String sql = "insert into member(name) values(?)";

        // 데이터베이스의 커넥션을 가지고온다. > Connection conn = dataSource.getConnection();
        // import java.sql.Connection; 임폴트.

        // prepareStatement() 메소드를 사용하여 문장을 합성. 매개변수 sql 설정.
        // PrepareStatement 객체인 pstmt 변수 생성. > PreparedStatement pstmt = connection.prepareStatement(sql);

        /*  변수인 pstmt에 setString(parameterIndex, String x) 메서드 사용하여
            매개변수로 첫번째의, 함수 매개변수로받은 member의 name을 get메서드 사용해서 꺼내고 세팅해줌.
            >  pstmt.setString(1, member.getName()); */

        // executeUpdate() 메서드를 사용하면 디비에 쿼리가 날라간다. > pstmt.executeUpdate();

        
        String sql = "insert into member(name) values(?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        // import java.sql.ResultSet; 임포트
        // 이 resultSet은 결과를 받는 객체이다.
        ResultSet rs = null;

        try {
            conn = getConnection();

            // import java.sql.Statement; 임포트
            /* 커넥션에서 prepareStatement에서 sql을 넣고,
               두번째 매개변수인 Statement.RETURN_GENERATED_KEYS 라는 특별한 옵션이 하나있다.
               이것은 우리가 디비에 인설트를 해야 name값을 얻을 수 있는 것처럼
               그것을 할때 사용할 수 있는 것이다. */
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // sql쿼리문의 ? 첫번째 물음표와 매칭되어 값이 바인딩됨.
            pstmt.setString(1, member.getName());
            // 디비에 실제 쿼리가 날라감.
            pstmt.executeUpdate();
            /* getGeneratedKeys()를 사용하여 꺼낼 수 있는데,
               상단의 매개변수에서 Statement.RETURN_GENERATED_KEYS
	           이것을 추가하여 사용할 수 있는것이다.
	           예를들면 디비가 방금 내가 키를(id) 생성했을때 1번으로 생성되면 1번으로 반환한다.
	           2번으로 생성되면 2번으로 반환한다. */
            rs = pstmt.getGeneratedKeys();
            /* resultSet에서 값을 가지고 있다.
               처음 rs.next()를 하여 값이 있으면
               getLong()를 사용하여 값을 꺼내어 member.SetId()에 세팅해주면 된다.
               성공하면 잘되고, 실패하면 id 조회 실패 문구 표시됨.
               구현한것들이 Exception을 엄청 많이 던진다.
               그래서 try{} catch {} 를 엄청 잘해놔야 한다! */
            if (rs.next()) {
                member.setId(rs.getLong(1));
            } else {
                // import java.sql.SQLException 임포트
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            /* 그리고 사용한 자원들을 릴리즈 해줘야한다.
               디비 커넥션을 사용할 경우 네트워크가 연결된거기 때문에
               바로 끊어주며 리소스를 다 반환해야 한다. 안그러면 큰일난다.
               데이터베이스 커넥션 계속 쌓이다가 대 장애가 날 수도 있다.
               close() 리소스 하는것도 되게 복잡하다.
               소스를 역순으로 null이아니면 close()해주고,
               prepareStatement가 null이 아니면 close() 해주고,
               connection이 또 null이 아니면 close() 해주고,
               이런 복잡한 과정을 거쳐야 한다. */
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {

        // 조회를 위해 select 쿼리 저장.3
        String sql = "select * from member where id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            // sql 쿼리문 저장.
            pstmt = conn.prepareStatement(sql);
            // prepareStatement id 세팅.
            pstmt.setLong(1, id);

            // 조회시 메서드는 executeQuery() 메서드 사용. resultSet에 저장.
            rs = pstmt.executeQuery();

            // resultSet이 값이 있으면
            if(rs.next()) {
                // member객체 id, name 세팅.
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                // member 반환.
                return Optional.of(member);

                // resultSet 값이 없으면
            } else {
                //Optional 비우기 메서드 실행.
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        //
        String sql = "select * from member where name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    private Connection getConnection() {
        // import org.springframework.jdbc.datasource.DataSourceUtils; 임포트
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Member> findAll() {
        // 모든 자료를 조회해야 하기때문에 쿼리가 더 단순함.
        String sql = "select * from member";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            // import java.util.ArrayList; 임포트.
            // List member객체 생성.
            List<Member> members = new ArrayList<>();
            // resultset이 값이 있으면 반복
            while(rs.next()) {
                // member 객체 생성하여 값 세팅하며 반복.
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                // 반복하며 List에 member객체 add() 메서드 사용하여 List에 추가하기.
                members.add(member);
            }
            // members List객체인 member 반환.
            return members;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }

    }

    /* 커넥션을 닫을때는 dataSourceUtils를 통해서
        releaseConnection(conn, dataSource); 메서드를
        사용하여 close 해줘야한다. */
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }

}
