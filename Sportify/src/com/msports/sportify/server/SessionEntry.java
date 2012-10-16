/* Copyright (c) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.msports.sportify.server;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SessionEntry {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private int stepsToday;

	@Persistent
	private Date date;

	public SessionEntry(int stepsToday, Date date) {
		this.stepsToday = stepsToday;
		this.date = date;
	}
	
	public int getStepsToday() {
		return stepsToday;
	}
	
	public Long getId() {
		return id;
	}

	public static void insert(int stepsToday, Date date) {
		SessionEntry entry = new SessionEntry(stepsToday, date);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(entry);
	}

	public static List<SessionEntry> getEntries() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(SessionEntry.class);
		query.setOrdering("date DESC");
		List<SessionEntry> entries = (List<SessionEntry>) query.execute();
		return entries;
	}
}
