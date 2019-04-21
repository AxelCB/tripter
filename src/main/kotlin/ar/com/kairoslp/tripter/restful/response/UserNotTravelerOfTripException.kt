package ar.com.kairoslp.tripter.restful.response

class UserNotTravelerOfTripException(message: String): Exception(message) {

    constructor(userId: Long, tripId: Long) : this("The user with id $userId is not a traveler of trip with id $tripId")

    constructor(userIds: List<Long>, tripId: Long) : this("The user with ids " + userIds.joinToString(",") + " are not travelers of trip with id " + tripId)
}