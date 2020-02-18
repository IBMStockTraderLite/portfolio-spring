/*
       Copyright 2017-2019 IBM Corp All Rights Reserved

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.ibm.sample.stocktrader.portfolio.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ibm.sample.stocktrader.portfolio.model.Client;

@Repository
public class JdbcClientRepository implements ClientRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int save(Client client) {
		return jdbcTemplate.update(
				"insert into client (clientId, firstName, lastName, email, preferredContactMethod) values(?,?,?,?,?)",
				client.getClientId(), client.getFirstName(), client.getLastName(), client.getEmail(),
				client.getPreferredContactMethod());
	}

	@Override
	public Optional<Client> findById(String id) {
		return jdbcTemplate.queryForObject("select * from client where clientid = ?", new Object[] { id },
				(rs, rowNum) -> Optional.of(new Client(rs.getString("clientid"), rs.getString("firstName"),
						rs.getString("LastName"), rs.getString("email"), rs.getString("preferredContactMethod")) ));
	}

}
