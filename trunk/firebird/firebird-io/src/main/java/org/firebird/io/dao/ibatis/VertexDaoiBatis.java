package org.firebird.io.dao.ibatis;

import org.apache.ibatis.session.SqlSession;
import org.firebird.dao.ibatis.GenericDaoiBatis;
import org.firebird.io.dao.VertexDao;
import org.firebird.io.model.Vertex;

public class VertexDaoiBatis extends GenericDaoiBatis implements VertexDao {

	public VertexDaoiBatis() {
	}

	public void insertVertex(Vertex vertex) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.insert("ibatis.sqlmap.Vertex.insertVertex", vertex);
		} finally {
			session.close();
		}
	}
}
