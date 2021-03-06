package metier;

import java.util.*;

public class PyRat {
    private List<Point> fromages;
    private Map<Point, List<Point>> laby;
    private Set<Point> fromagesSet;
    private Map<Point, Set<Point>> labySet;
    private List<Point> atteint;
    private List<Point> chemin;
    private int labyWidth;
    private int labyHeight;
    /* Méthode appelée une seule fois permettant d'effectuer des traitements "lourds" afin d'augmenter la performace de la méthode turn. */
    public void preprocessing(Map<Point, List<Point>> laby, int labyWidth, int labyHeight, Point position, List<Point> fromages) {
        this.labyHeight = labyHeight;
        this.labyWidth = labyWidth;
        this.fromages = fromages;
        this.fromagesSet = new HashSet<>(fromages);
        this.labySet = new HashMap<>();
        this.laby = laby;
        for(Point key: laby.keySet()){
            Set<Point> values = new HashSet<>(laby.get(key));
            labySet.put(key, values);
        }
    }

    /* Méthode de test appelant les différentes fonctionnalités à développer.
        @param laby - Map<Point, List<Point>> contenant tout le labyrinthe, c'est-à-dire la liste des Points, et les Points en relation (passages existants)
        @param labyWidth, labyHeight - largeur et hauteur du labyrinthe
        @param position - Point contenant la position actuelle du joueur
        @param fromages - List<Point> contenant la liste de tous les Points contenant un fromage. */
    public void turn(Map<Point, List<Point>> laby, int labyWidth, int labyHeight, Point position, List<Point> fromages) {
        Point pt1 = new Point(2,1);
        Point pt2 = new Point(3,1);
        System.out.println((fromageIci(pt1) ? "Il y a un" : "Il n'y a pas de") + " fromage ici, en position " + pt1);
        System.out.println((fromageIci_EnOrdreConstant(pt2) ? "Il y a un" : "Il n'y a pas de") + " fromage ici, en position " + pt2);
        System.out.println((passagePossible(pt1, pt2) ? "Il y a un" : "Il n'y a pas de") + " passage de " + pt1 + " vers " + pt2);
        System.out.println((passagePossible_EnOrdreConstant(pt1, pt2) ? "Il y a un" : "Il n'y a pas de") + " passage de " + pt1 + " vers " + pt2);
        System.out.println("Liste des points inatteignables depuis la position " + position + " : " + pointsInatteignables(position));
    }

    /* Regarde dans la liste des fromages s’il y a un fromage à la position pos.
        @return true s'il y a un fromage à la position pos, false sinon. */
    private boolean fromageIci(Point pos) {
        return this.fromages.contains(pos);
    }

    /* Regarde de manière performante (accès en ordre constant) s’il y a un fromage à la position pos.
        @return true s'il y a un fromage à la position pos, false sinon. */
    private boolean fromageIci_EnOrdreConstant(Point pos) {
        return this.fromagesSet.contains(pos);
    }

    /* Indique si le joueur peut passer de la position (du Point) « de » au point « a ».
        @return true s'il y a un passage depuis  « de » vers « a ». */
    private boolean passagePossible(Point de, Point a) {
        if(laby.containsKey(de)){               //Si la position de est dans le labyrinthe
            return laby.get(de).contains(a);    //Retourne true ou false si le point est atteignable
        }
        return false;
    }

    /* Indique si le joueur peut passer de la position (du Point) « de » au point « a »,
        mais sans devoir parcourir la liste des Points se trouvant dans la Map !
        @return true s'il y a un passage depuis  « de » vers « a ». */
    private boolean passagePossible_EnOrdreConstant(Point de, Point a) {
        if(this.labySet.containsKey(de)){               //Si la position de est dans le labyrinthe
            return this.labySet.get(de).contains(a);    //Retourne true ou false si le point est atteignable
        }
        return false;
    }

    /* Retourne la liste des points qui ne peuvent pas être atteints depuis la position « pos ».
        @return la liste des points qui ne peuvent pas être atteints depuis la position « pos ». */
    private List<Point> pointsInatteignables(Point pos) {
        //Initialisation des listes
        atteint = new ArrayList<>();
        chemin = new ArrayList<>();
        List<Point> nonAtteint = new ArrayList<>();
        parcoursRec(pos);                           //Parcours récursif du labyrinthe (Remplissage des points atteint)
        for(int i = 0; i < labyWidth; i++){         //Boucle longueur
            for(int y = 0; y < labyHeight; y++){    //Boucle hauteur
                Point pt = new Point(i,y);          //Création d'un nouveau point
                if(!atteint.contains(pt)){          //Si le point n'est dans la liste des points atteints
                    nonAtteint.add(pt);             //Ajout dans non atteignable
                }
            }
        }
        return nonAtteint;                          //Retourne la liste
    }

    private void parcoursRec(Point pt){
        chemin.add(pt);                             //Ajout du point (Marquage)
        for(Point voisin : laby.get(pt)){           //Boucle des voisins
            if (!chemin.contains(voisin)){          //Si le chemin ne contient pas le voisin
                atteint.add(voisin);                //Ajout aux points atteint
                parcoursRec(voisin);                //Parcours récursif
            }
        }
        chemin.remove(pt);                          //Suppression de la liste des visité
    }

}