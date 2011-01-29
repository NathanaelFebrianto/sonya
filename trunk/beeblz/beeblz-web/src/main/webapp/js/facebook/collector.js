/**
 * Copyright Beeblz.com.
 *
 */

function getMyFriends(response) {
    FB.api(
      {
        method: 'fql.query',
        query: 'SELECT name, picture FROM user WHERE uid=' + FB.getSession().uid
      },
      function(response) {
        var user = response[0];
        return user;
      }
    );
}
