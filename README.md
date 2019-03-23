# MoulinIIA

## Pour changer la position de l'ia, ou jouer a deux joueur
changer la position de l'ia : 
* changer la ligne 51 de Board.java le parametre 2 de ordinateur, en 1 ou 0 en fonction de qui joue quand

pour jouer contre un joueur :
* changer la ligne 51 la mettre en commentaire 

## Pour executer
/usr/lib/jvm/java-8-openjdk-amd64/bin/java -javaagent:/home/jromary/.local/share/JetBrains/Toolbox/apps/IDEA-U/ch-0/183.5912.21/lib/idea_rt.jar=39449:/home/jromary/.local/share/JetBrains/Toolbox/apps/IDEA-U/ch-0/183.5912.21/bin -Dfile.encoding=UTF-8 -classpath /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/charsets.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/cldrdata.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/dnsns.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/icedtea-sound.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/jaccess.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/java-atk-wrapper.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/jfxrt.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/localedata.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/nashorn.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunec.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunjce_provider.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/sunpkcs11.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/zipfs.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jce.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jfxswt.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jsse.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/management-agent.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/resources.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar:/home/jromary/Programmation/Java/MoulinIA/out/production/moulin moulin.Main

## Pour lancer le projet
Ouvrir le projet sur intelliJ et lancer la classe main


## Heurestique
Coeficien de la fonction d'evaluation

    int[] coef = {2, -1, 4, 3}

Fonction d'evaluation

    eval = 
        (coef[0] * (nbpairAlLibreOrdi - nbpairAlLibreNOrdi)) +
        (coef[1] * (nbpairAlMalOrdi - nbpairAlMalNOrdi)) +
        (coef[2] * (nbMillOrdi - nbMillNOrdi)) +
        (coef[3] * (nbMillBlockOrdi - nbMillBlockNOrdi))
        
On verifie le nombre de pair de jeton aligné avec la posibiliter de faire un moulin, le nombre de pair 
qui sont mal aligné, le nombre de moulin effectuer, et enfin le nombre de moulin enemie bloqué.

## Evaluation personnel


|                               | OUI   | NON | 
|----------                     |:-----:|:-----:| 
Interface Textuel               |       | X
Homme / machine                 | X     |
Machine / Machine               |   X   | X
Homme / Homme                   | X     |
Client - Serveur                | X     |   
Heurestique                     | X     |
Placement des pion deploiment   | X     |
Placement des pion Inteligeament|   X   |   X
Gestion Correct des Regle de fin|   X   |   
Profondeur > 1                  |   X   |
Gestion de l'historique de etats|   X   |
Tirage aleatoire des sol equivalente| X |
Code commenté                   |   X   |   X

Suplément:
- Utilisation de thread pour aller plus vite dans l'exploration des etats
- Utilisation d'une hashMap concurentiel pour allez avec les threads
- Profondeur de recherche de 7 quand facteur de branchement grand
- Profondeur de recherche de 9 quand le facteur de branchement est plus faible
- Possibilité de deselctioner un jeton lors du mouvement
- Implementation de Client - Serveur Non tester car pas d'autre personne avec qui le faire
    - Normalement je peut utiliser cela pour faire des duel Machine / machine (je n'ai pas reussi)
    
# Index
Source GitHub : https://github.com/Jromary/MoulinIIA
 



