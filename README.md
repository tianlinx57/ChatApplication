# SR03 - Application de chat
SR03  Devoir2   P23 

Auteur: Linxiao TIAN, Xingyu DU

<br>

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

![Architecture](/img/architecture.png)

La première partie, basé sur Spring, joue un double rôle. Il englobe à la fois les services et les pages web liés à l'interface d'administrateur, ainsi que les services web destinés au projet React.

En ce qui concerne l'interface d'administrateur, elle est responsable de la gestion des utilisateurs. Elle repose sur l'architecture MVC (Modèle-Vue-Contrôleur) et utilise diverses technologies, telles que les contrôleurs Spring, JPA et ORM pour accéder aux données, et HTML/Thymeleaf, CSS, JS (Jquery, Bootstrap, etc.) pour la vue. Et le serveur interagit bien avec une base de données phpMyAdmin. 
 
Les fonctionnalités de l'application d'administration comprennent la création d’un nouveau utilisateurs, la modification de leurs informations(nom, prénom, email, mot de passe, isAdmin), la suppression d'un utilisateur, la désactivation ainsi que la recherche de ces utilisateurs.


Voici l'interface administrateur:

![interface administrateur](/img/interface%20admin.png)

Pour la deuxième partie, le chat de discussion, il faut réaliser la gestion des échanges de messages en temps réel, ainsi que de la gestion du cycle de vie des salons de discussion. Il est développé avec *React* et utilise le protocole *WebSocket* pour recevoir et diffuser des messages sur le canal approprié. 

Les utilisateurs ont leur droit de créer, modifier ou supprimer les chats. Ils peuvent aussi inviter d'autres utilisateurs à leurs salons de discussion.


Voici l'interface utilisateur: 

![interface utilisateur](/img/interface%20utilisateur.png)



## Conception

<br>

![Diagramme](/img/UML.png)


Comme vous pouvez le constater dans le diagramme, nous avons principalement conçu deux classes, User et Chat.

La classe **User** représente un utilisateur de l'application. Il dispose des attibuts comme ci-dessous. Les champs mail et password permettent à l'utilisateur de se connecter à l'application. Le champ admin permet de l'utilisateur d’entrer dans l'interface administrateur. Le champ isDisabled représente la désactivation de l'utilisateur. La classe User contient aussi des attributs pour le nom, le prénom, un identifiant unique, chats_user et chats_prioritaire. 

La classe **Chat** représente un salon de discussion. Elle contient un id, un nom, une description, une date de début, une deadline, son users et son propriétaire. Nous avons aussi crée des getters et des setters pour eux.

Quant à la relation entre eux, nous pensons qu'il s'agit de plusieurs à plusieurs. Un utilisateur peut avoir plusieurs chats en même temps, et il peut y avoir plusieurs utilisateurs différents dans un chat.

Il est à noter que dans la classe chat, nous avons conçu l'attribut propreietaire. Chaque chat ne peut avoir qu'un seul propriétaire.

En outre, nous avons conçu une classe **chatDTO**, qui est utilisé comme variable pour stocker les paramètres de chat dans les données lorsque l'application *REACT* appelle le backend *RESTApi* pour créer et mettre à jour le chat. Il est avantageux pour le backend d'utiliser la variable entière comme *RequestBody* pour extraire les paramètres de chat. En gros, c'est une variable intermédiaire dans la communication entre frontend et backend.



## Interactions entre technologies

Pour réaliser ce projet, on a utilisé de nombreuses technologies différentes. Ensuite, on va présenter brièvement les technologies react, spring et websocket utilisées dans ce projet et leur interaction.

Tout d'abord, on a utilisé la technologie **Spring Boot** pour implémenter l'interface administrateur. Il s'exécute sur localhost:8080. On a également effectué des opérations d'authentification. Par exemple, si une personne entre dans l'interface d'accueil sans être identifiée, elle sera redirigée vers l'interface de connexion pour l'opération de connexion. Si la connexion est authentifiée et que l'utilisateur est administrateur, une session est ouverte et l'utilisateur peut accéder à l'interface administrateur.

Pour l'interaction avec le frontal, on a créé un **ChatWebSocketEndpoint**. Il s'agit d'un point de terminaison (endpoint) WebSocket côté serveur qui gère la communication en temps réel avec les clients. Il contient différentes méthodes annotées, telles que **@OnOpen** (appelée lorsque la connexion WebSocket est ouverte), **@OnMessage** (appelée lorsqu'un message est reçu) et **@OnClose** (appelée lorsque la connexion WebSocket est fermée), etc. Il contient également des méthodes auxiliaires pour envoyer et diffuser des messages aux utilisateurs de la salle de discussion.

On a utilisé aussi **RESTapiController**. Il s'agit d'un contrôleur API REST qui gère les requêtes HTTP. Il contient des méthodes de routage pour gérer les utilisateurs et les données de chat, telles que récupérer les chats d'un utilisateur, créer un chat, mettre à jour un chat et supprimer un chat, etc. De plus, on utilise @CrossOrigin(origins = "http://localhost:3000") pour garantir un accès interdomaine correct.

Deuxièmement, on a utilisé la technologie **React** pour implémenter l'interface utilisateur. Il s'exécute sur localhost:3000. Si un utilisateur visite localhost:3000, il doit s'authentifier de la même manière qu'un projet Spring. Il est à noter que cette partie n'est pas indépendante non plus. Il utilise les services Web fournis par l'API Rest du projet Spring. Par exemple, on utilise la bibliothèque axios pour envoyer des requêtes POST/GET à http://localhost:8080/api/.

Enfin, on a également appliqué des **Websockets**. Ils permettent aux utilisateurs de communiquer entre eux via le chat.  Dans le composant **ChatPage**，Il établit une connexion **WebSocket** avec le point de terminaison WebSocket côté serveur（backend） et utilise des requêtes HTTP pour obtenir les informations et les messages de chat en temps réel.  Le point de terminaison WebSocket côté serveur reçoit le message et le diffuse aux autres utilisateurs de la salle de discussion.  Lorsque l'utilisateur ferme la page de chat ou quitte la salle de discussion, la connexion WebSocket est fermée.

En résumé, **React**, **Spring** et **WebSocket** travaillent en synergie dans notre projet pour offrir une expérience utilisateur interactive et en temps réel. React gère l'interface utilisateur, Spring facilite le développement côté serveur et la communication avec l'API, tandis que les WebSockets permettent une communication bidirectionnelle et en temps réel entre React et Spring.



## Analyse éco-responsable de l'application


Grâce à l'utilisation du plug-in **GreenIT** de notre navigateur Chrome, nous avons pu évaluer l'impact environnementale de notre application.

Vous pouvez voir les captures d'écrans réalisés par GreenIT sur:

Les résultats pour la page admin:

![éco-responsable admin](/img/eco1.png)

Les résultats pour la page utilisateur:

![éco-responsable user](/img/eco2.png)

Les résultats pour l'histoire:

![éco-responsable history](/img/eco3.png)


Les résultats sont généralement positifs. Toutes les deux obtiennent le score "A", démontrant une bonne qualité en termes d'éco-responsabilité.



## Pistes d'amélioration

1. Dans l'interface de admin, lorsque nous voulons modifier les informations d'un utilisateur, nous souhaitons entrer le mot de passe par défaut pour éviter de les saisir à chaque fois. Cependant, notre application Spring Boot utilise BCryptPasswordEncoder pour chiffrer les mots de passe stockés dans la base de données, de sorte que la page de modification ne peut afficher que le mot de passe chiffré.

2. En raison de contraintes de temps, lors de la création de l'application React, nous n'avons pas séparé de nombreux composants réutilisables, mais les mettons dans le composant de la page. 
 
3. Dans le processus de communication WebSocket de notre page de chat, les connexions, les déconnexions des utilisateurs et les messages normaux sont tous affichés de la même manière dans la zone de chat. Bien que la couleur de la carte utilisateur ait été modifiée lors de la connexion et de la déconnexion, vous pouvez essayer d'afficher ces deux types de messages système à l'aide d'autres types de composants pour améliorer la richesse et la lisibilité de la zone de chat.
 