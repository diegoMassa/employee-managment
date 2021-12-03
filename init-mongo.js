db.createUser(
	{
		user: "mongo",
		pwd:  "secret",
		roles: [
			{
				role: "readWrite",
				db:   "mongodb"
			}
		]
	}
)