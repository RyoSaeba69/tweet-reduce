# tweet-reduce
Tendance on Tweeter.

Pour installer gradle : `brew install gradle`

Pour compiler et créer un jar : `gradle jar`

Le jar est généré dans build/libs

Pour lancer le jar : `hadoop tweet-reduce-x.x.jar`

Ajouter : la classe OurCredentials.java

import java.lang.String;

public class OurCredentials {
    public static String ACCESS_TOKEN = ACCESS_TOKEN;
    public static String ACCESS_TOKEN_SECRET = ACCESS TOKEN SECRET;
}
