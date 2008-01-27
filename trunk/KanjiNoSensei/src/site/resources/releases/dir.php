<?
// Vous pouvez changer le chemin de départ, par default "." //
$depart=".";

// ----- Affiche arborescence ----- //

require("./fonctions.php");

// ------------------------------ Initialisation des variables ----------------------------------------------- //

//if(!empty($chemin))	$chemin     = str_replace("..","dev/null",$chemin);
//$chemin = "../omega/" . @$chemin;


if(!empty($chemin))     $chemin     = stripslashes($chemin); else $chemin = $depart;


// ----------------------------------- Sécurité navigation -------------------------------------------------- //

$chemintotal = VerifCheminFils($chemin);

if ($chemintotal === false)
{
	
	$chemintotal = $depart;
}

// ------------------------------- Récupération des fichiers et répertoires dans tableau-- //

$repind  = 0;
$fileind = 0;

if ($handle = @opendir($chemintotal))
{
	while (false !== ($file = @readdir($handle)))
	{	
		if ($file != "." && $file != "..")
		{
			if(is_dir("$chemintotal/$file"))
			{
				if ($file == "img")
				{
					continue;
				}

				$reptab[$repind]["nom"]           = $file;
				$reptab[$repind]["taille"]        = filesize("$chemintotal/$file");
				$repind++;
			}
			else
			{
				if (eregi(GetExtension($file), "php, htaccess, ftpquota"))
				{
					continue;
				}

				$filetab[$fileind]["nom"]         = $file;
				$filetab[$repind]["taille"]        = filesize("$chemintotal/$file");
				$fileind++;
			}
		}
	}
	closedir($handle);
}
@closedir($handle);

	

// ============================= Affichage =================================== //
?>

<! ----------------------- Affichage du chemin ------------------>
<table width=50% border="0" cellspacing="0" cellpadding="0" BGCOLOR="#b0bace">
  <tr>
    <td><b><? echo " Chemin : ";?></b><?$CheminDecompose = DecomposerChemin($chemintotal);echo $CheminDecompose; // -- Affiche le dossier courant -- // ?>
  </td>
  </tr>
</table>

<TABLE WIDTH="650" BORDER="0" CELLPADDING="0" CELLSPACING="1" nowrap>
<TR>
	<TD background="/img/menu.jpg" width=35>&nbsp;</TD>
	<TD background="/img/menu.jpg" ALIGN="center"><b><? echo "Nom"; ?></b></TD>
	<TD background="/img/menu.jpg" width=65 ALIGN="center" COLSPAN="5"><B><? echo "Taille"; ?></B></TD>
</TR>
<TR><TD COLSPAN="10"><HR NOSHADE></TD></TR>

<? 
// ------ Si on clique sur dossier parent --------------- //

$cheminretour = ModifChemin($chemintotal);
$cheminretour = rawurlencode($cheminretour);

$lienretour = ($chemintotal == $depart."/")?"..":"./dir.php?chemin=".$cheminretour;
?>
<TR>
	<TD width=35 ALIGN="center"><A HREF="<? echo $lienretour; ?>"><IMG SRC="./img/back.gif" BORDER="0"></A></TD>
	<TD ALIGN="left"  ><A HREF="<? echo $lienretour; ?>">..</A></TD>
</TR>
<?


// -------------------------------------- Affichage des répertoires --------------------------------------- //

for($i=0;$i<$repind;$i++)
{
	$nomrep      = $reptab[$i]["nom"];
	$cheminrep   = rawurlencode($chemintotal."/".$nomrep);
	$IndiceImage = $i;
	$pair	     = $i%2;
	?>
	<TR>
		<TD <? if ($pair==1) {echo "";?>BGCOLOR="#b0bace" bordercolor="#b0bace"<?} else {echo "";?>BGCOLOR="#d8dde7" bordercolor="#d8dde7"<?}?> ALIGN="center"><A HREF="./dir.php?chemin=<? echo $cheminrep; ?>"><IMG SRC="./img/dir.gif" border="0"></A></TD>
		<TD <? if ($pair==1) {echo "";?>BGCOLOR="#b0bace" bordercolor="#b0bace"<?} else {echo "";?>BGCOLOR="#d8dde7" bordercolor="#d8dde7"<?}?> ALIGN="left" ><font color="#000099"><font face=" Verdana,Arial, Helvetica, sans-serif" size="2"><? echo $nomrep; ?></TD>
		<TD <? if ($pair==1) {echo "";?>BGCOLOR="#b0bace" bordercolor="#b0bace"<?} else {echo "";?>BGCOLOR="#d8dde7" bordercolor="#d8dde7"<?}?> ALIGN="left" ><font color="#000099"><font face=" Verdana,Arial, Helvetica, sans-serif" size="2"></TD>

	</TR>
	<?
}

// --------------------------------------- Affichage des fichiers ----------------------------------------- //

$IndiceImage++;


for($i=0;$i<$fileind;$i++)
	{
	$nomfic      = $filetab[$i]["nom"];
	$ext         = GetExtension($nomfic);
	$ext         = strtolower($ext);
	$icone       = GetIcone($ext);
	$IndiceImage = $i;
	$pair	     = $i%2;

	?>
	<TR>
	<?
		if ($pair == 1)
		{
			$BGCOLOR = "#F2F4F7";
			$BORDERCOLOR = "#F2F3F7";
		}
		else
		{
			$BGCOLOR = "#EBEEF3";
			$BORDERCOLOR = "#EBEEF3";
		}
	?>
		<TD BGCOLOR="<?php echo $BGCOLOR; ?>" BORDERCOLOR="<?php echo $BORDERCOLOR; ?>" width=35 ALIGN="center"><IMG SRC ="./img/<? echo $icone ?>"></TD>
		<TD BGCOLOR="<?php echo $BGCOLOR; ?>" BORDERCOLOR="<?php echo $BORDERCOLOR; ?>" ALIGN="left"  ><A HREF="download.php?fichier=<? echo $chemintotal."/".$nomfic; ?>"><? echo $nomfic ; ?></A></TD>
		<TD BGCOLOR="<?php echo $BGCOLOR; ?>" BORDERCOLOR="<?php echo $BORDERCOLOR; ?>" ALIGN="right"  ><? echo FormatTailleFichier(filesize($chemintotal."/".$nomfic)); ?></TD>
	</TR>
	<?
	}

// ------ fin du tableau ---- //

?>
<TR><TD COLSPAN="10"><HR NOSHADE></TD></TR>
</TABLE><BR>


<! --------- Affiche le nombre de dossiers et de fichiers -------------- >


<table width=400 border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td background="/img/menu.jpg"><B>
	<? echo " Nombre de dossier(s) : ".$repind; ?> - <? echo " Nombre de fichier(s) : ".$fileind; ?></B>
	</td>
  </tr>
</table>

</BODY>
</HTML>
