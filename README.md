## 要求
Une description de l'architecture de l'application que vous proposez  (un schéma avec sa description).

La conception (diagramme de classes, schéma relationnel, justifier brièvement vos choix)

Explications sur les interaction entre les différente technologies : react, spring et web socket

Résultat eco-index de votre site (plugin greenit) et des pistes d'amélioration
# SR03 - Application de chat
SR03  Devoir2   P23 

Auteur: Linxiao TIAN, Xingyu DU

## Objectif du projet

Le projet a comme finalité le développement d'une application de salon de discussion (chat). Dans cette application, chaque utilisateur peut programmer un canal de discussion (un chat) à une date avec une durée de validité et inviter au moins un autre utilisateur.

On ne souhaite pas sauvegarder les messages (contenu + heure) des canaux de discutions mais ils
seront seulement diffusés sur les sessions (canaux connectés) de tous les utilisateurs connectés à ce chat. Une personne qui se connecte tardivement à un canal ne verra pas les messages publiés 
antérieurement.

Un canal de discussion a un identifiant unique, un titre, une description, une date avec un horaire et une durée de validité.

La gestion des utilisateurs (ajout, suppression désactivation) se fait via une interface d’administration 
qui est accessible uniquement aux utilisateurs de type admin. Un utilisateur a un nom, un prénom, une adresse mail, un mot de passe, un champ admin (vrai ou faux)

## Architecture du projet

L'application web développée pour ce devoir se compose principalement de deux parties, l'interface administrateur et l'interface de chat utilisateur. Pour les implémenter, nous utilisons respectivement Spring et React, qui interagissent entre eux, comme l'illustre le schéma ci-dessous.
## TODO
![Architecture](/img/Architecture.jpg)

 La première partie, basé sur Spring, joue un double rôle. Il englobe à la fois les services et les pages web liés à l'interface d'administrateur, ainsi que les services web destinés au projet React.



En ce qui concerne l'interface d'administrateur, elle est responsable de la gestion des utilisateurs. Elle repose sur l'architecture MVC (Modèle-Vue-Contrôleur) et utilise diverses technologies, telles que les contrôleurs Spring, JPA et ORM pour accéder aux données, et HTML/Thymeleaf, CSS, JS (Jquery, Bootstrap, etc.) pour la vue. Et le serveur interagit bien avec une base de données phpMyAdmin. 

### JWT??
L'accès à ces contrôleurs est soumis à une authentification de l'utilisateur, qui doit être obligatoirement un administrateur. 
Les contrôleurs REST utilisés par l'application React nécessitent quant à eux la présentation d'un jeton **JWT (JSON Web Token)** et renvoient des objets JSON. 
Les fonctionnalités de l'application d'administration comprennent la création d’un nouveau utilisateurs, la modification de leurs informations(nom, prénom, email, mot de passe, isAdmin), la suppression d'un utilisateur, la désactivation ainsi que la recherche de ces utilisateurs.

Interface administrateur:

![interface administrateur](/img/interface%20admin.png)

Pour la deuxième partie, le chat de discussion, il faut réaliser la gestion des échanges de messages en temps réel, ainsi que de la gestion du cycle de vie des salons de discussion. Il est développé avec React et utilise le protocole WebSocket pour recevoir et diffuser des messages sur le canal approprié. 

Les utilisateurs ont leur droit de créer, modifier ou supprimer les chats. Ils peuvent aussi inviter d'autres utilisateurs à leurs salons de discussion.

## ??jwt
L'utilisation d'un token JWT fourni lors de la connexion permet de consommer les services web de manière sécurisée dans le projet Spring.

Dans le projet, les composants sont codés en fichiers TSX pour garantir l'intégrité des types de variables et faciliter l'intégration de code HTML.

Interface utilisateur: 

![interface utilisateur](/img/interface%20utilisateur.png)


## 别人的Conception

Si la structure générale du projet a été suggérée par le sujet du devoir, la phase de conception a elle été réalisée librement.

Le diagramme de classe contenu dans le dossier "conception" résume nos choix de conceptions et les liens entre nos différentes classes.

![Diagramme de classe](/conception/conception.png)

La classe **User** représente un utilisateur de l'application, avec les attributs exigés par le sujet.
Les champs *email* et *password* permettent notamment à l'utilisateur de se connecter à l'application.
Le champ *isAdmin* permet de rediriger l'utilisateur vers l'interface administrateur ou vers l'application de chat en fonction de sa valeur.
Le champ *isDeactivated* vaut vrai si l'utilisateur est désactivé. Dans ce cas, il ne peut plus utiliser l'application tant qu'il n'est pas débloqué par un administrateur.
La classe User contient aussi des attributs pour le nom et prénom de l'utilisateur, un identifiant unique généré automatiquement ainsi que des constructeurs, les setters et les getters.

La classe **Chat** représente un salon de discussion.
Elle contient un titre, une description, une date de début sous format LocalDateTime, une durée en minutes et l'identifiant de l'utilisateur propriétaire.
Les champs *date* et *durée* permettent de calculer si une discussion peut avoir lieu ou non dans le salon.
Le champ *ownerId*, l'identifiant du propriétaire, désigne le créateur du chat, qui a aussi des droits de modification sur l'objet.
La classe présente là aussi, et comme toutes les autres, des constructeurs, les setters et les getters.
Le sujet ne précisant pas comment la suppression d'un chat doit être gérée si sa date de validité est en cours, on a décidé d'interdire la suppression des chats de ce cas là.

La classe **Invitation** du diagramme, appelée **ChatUser** dans le projet contient l'ensemble des invitations aux chats auxquels les utilisateurs ont été invités. Cela se traduit par l'utilisation de deux attributs faisant office de clé primaire : *userId* et *chatId*.
Elle permet de dresser la liste des utilisateurs participants à un chat, à laquelle il faut ajouter l'utilisateur propriétaire.

La classe **Attempt** compte le nombre de tentatives erronées de connexions des utilisateurs.
On a choisi de séparer cette classe de la classe **User** estimant que cette dernière pourrait évoluer dans certains contextes sans ce compteur de mauvaises connexions.
Elle contient les champs *userId* et *nbAttemps*. Dans l'application, lorsque *nbAttemps* atteint la valeur de 5, l'utilisateur dont l'adresse email est entrée dans le formulaire de connexion devient désactivé et ne peut plus se connecter.
Cette précaution prévient des attaques de nature dite *brut force*.

Enfin, la classe **WebSocket** apparaissant dans le diagramme ne fait pas partie du modèle à proprement parler de l'application, mais permet la communication entre utilisateurs sur les salons de discussion.
Elle répartit les messages reçus par une session aux autres sessions correspondant au même chat, mais à des utilisateurs différents.
Le formatage des messages circulant au format JSON permet de savoir qui est l'envoyeur et à quel chat il s'adresse. Il permet aussi de savoir quand un utilisateur se connecte ou se déconnecte du chat.

On notera que pour toute cette partie conception, aucune clé étrangère n'est réellement utilisée. Par exemple, rien dans la structure des modèles ne permet de déterminer que *ownerId* correspond effectivement à un utilisateur existant. Cela est dû au fait que l'utilisation de telles méthodes n'a pas été abordée avec l'équipe enseignante. Il s'agit évidemment d'une piste d'amélioration.

Aussi, il a été décidé de manier des relations de composition entre les objets.
Cela engendre par exemple que si un utilisateur est supprimé par l'administrateur, ses invitations aux chats le sont aussi, tout comme les chats dont il est propriétaire et son nombre de tentatives de connexions. De même, si un chat est supprimé, les invitations associées le sont aussi.


# Interactions entre technologies

Pour réaliser ce projet, on a utilisé de nombreuses technologies différentes. Ensuite, on va présenter brièvement les technologies react, spring et websocket utilisées dans ce projet et leur interaction.

Tout d'abord, on a utilisé la technologie **Spring Boot** pour implémenter l'interface administrateur. Il s'exécute sur localhost:8080. On a également effectué des opérations d'authentification. Par exemple, si une personne entre dans l'interface d'accueil sans être identifiée, elle sera redirigée vers l'interface de connexion pour l'opération de connexion. Si la connexion est authentifiée et que l'utilisateur est administrateur, une session est ouverte et l'utilisateur peut accéder à l'interface administrateur.

Pour l'interaction avec le frontal, on a créé un **ChatWebSocketEndpoint**. Il s'agit d'un point de terminaison (endpoint) WebSocket côté serveur qui gère la communication en temps réel avec les clients. Il contient différentes méthodes annotées, telles que **@OnOpen** (appelée lorsque la connexion WebSocket est ouverte), **@OnMessage** (appelée lorsqu'un message est reçu) et **@OnClose** (appelée lorsque la connexion WebSocket est fermée), etc. Il contient également des méthodes auxiliaires pour envoyer et diffuser des messages aux utilisateurs de la salle de discussion.

On a utilisé aussi **RESTapiController**. Il s'agit d'un contrôleur API REST qui gère les requêtes HTTP. Il contient des méthodes de routage pour gérer les utilisateurs et les données de chat, telles que récupérer les chats d'un utilisateur, créer un chat, mettre à jour un chat et supprimer un chat, etc. De plus, on utilise @CrossOrigin(origins = "http://localhost:3000") pour garantir un accès interdomaine correct.

Deuxièmement, on a utilisé la technologie **React** pour implémenter l'interface utilisateur. Il s'exécute sur localhost:3000. Si un utilisateur visite localhost:3000, il doit s'authentifier de la même manière qu'un projet Spring. Il est à noter que cette partie n'est pas indépendante non plus. Il utilise les services Web fournis par l'API Rest du projet Spring. Par exemple, on utilise la bibliothèque axios pour envoyer des requêtes POST/GET à http://localhost:8080/api/.

Enfin, on a également appliqué des **Websockets**. Ils permettent aux utilisateurs de communiquer entre eux via le chat.  Dans le composant **ChatPage**，Il établit une connexion **WebSocket** avec le point de terminaison WebSocket côté serveur（backend） et utilise des requêtes HTTP pour obtenir les informations et les messages de chat en temps réel.  Le point de terminaison WebSocket côté serveur reçoit le message et le diffuse aux autres utilisateurs de la salle de discussion.  Lorsque l'utilisateur ferme la page de chat ou quitte la salle de discussion, la connexion WebSocket est fermée.

En résumé, **React**, **Spring** et **WebSocket** travaillent en synergie dans notre projet pour offrir une expérience utilisateur interactive et en temps réel. React gère l'interface utilisateur, Spring facilite le développement côté serveur et la communication avec l'API, tandis que les WebSockets permettent une communication bidirectionnelle et en temps réel entre React et Spring.



# Analyse éco-responsable de l'application


Grâce à l'utilisation du plug-in **GreenIT** de notre navigateur Chrome, nous avons pu évaluer l'impact environnementale de notre application.

Vous pouvez voir les captures d'écrans réalisés par GreenIT sur:

Les résultats pour la page admin:

![éco-responsable admin](/img/eco1.png)

Les résultats pour la page utilisateur:

![éco-responsable user](/img/eco2.png)

Les résultats pour l'histoire:

![éco-responsable history](/img/eco3.png)


Les résultats sont généralement positifs. Toutes les deux obtiennent le score "A", démontrant une bonne qualité en termes d'éco-responsabilité.



# Pistes d'amélioration

1. Dans l'interface d'administration, lorsque nous voulons modifier les informations de l'utilisateur, nous devons changer le mot de passe à chaque fois. Le mot de passe est stocké dans la base de données sous une forme cryptée, mais le mot de passe crypté n'est pas conforme à la spécification de mot de passe par défaut du système. Par conséquent, des problèmes inutiles peuvent survenir lors de la modification des informations utilisateur.

3. Le programme en cours ne peut se connecter qu'à des bases de données distantes. Nous pouvons modifier les configurations et le laisser se connecter à la base de données locale. Cela garantira la sécurité des données et facilitera la modification et le débogage.

5. Les aspects visuels de l'interface peuvent être rendus plus beaux pour améliorer l'expérience interactive de l'utilisateur.