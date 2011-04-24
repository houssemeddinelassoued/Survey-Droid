<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><?php echo $title_for_layout; ?></title>
	<?php
		echo $html->css('peoples');
		echo $html->meta('peoples.ico', '/img/peoples.ico', array('type' => 'icon'));
		echo $this->Html->script('jquery');
		echo $scripts_for_layout;
	?>
</head>
<body>


<div id="header">
	<h3>
	<a href="/"><?php echo $html->image('/img/peoples.ico');?>PEOPLES</a>
	</h3> 
	<div id="menubar">
		<?php
		if (($user = $session->read('Auth.User')) != NULL)
		{
			echo $html->link('Subjects', array('controller' => 'subjects', 'action' => 'index'));
			if ($user['admin'] == 1)
				echo $html->link('Control Pannel', array('controller' => 'users', 'action' => 'index'));
			echo $html->link('Data Collection', array('controller' => 'datas', 'action' => 'index'));
			echo $html->link('Surveys', array('controller' => 'surveys', 'action' => 'index'));
		}
		?>
	</div>
</div>
<div id="userInfo">
	<?php
		if (($user = $session->read('Auth.User')) != NULL)
		{
			echo $html->link('Logout', array('controller' => 'users', 'action' => 'logout'));
		}
		else
		{
			echo $html->link('Login', array('controller' => 'users', 'action' => 'login'));
		}
	?>
</div>
<div id="main">
	<hr />
	<?php echo $content_for_layout;?>
</div>
<div id="footer">
	<hr />
	<?php
		if (($user = $session->read('Auth.User')) != NULL)
		{
			echo '<p>Logged in as ';
			echo '<b>'.$user['first_name'].' '.$user['last_name'].', </b>';
			if($user['admin']==1)
				echo ' (Administrator) ';
			echo $html->link('Logout', array('controller' => 'users', 'action' => 'logout'));
			echo '</p>';
		}
		else
		{
			echo '<p>Please log in here: ';
			echo $html->link('Login', array('controller' => 'users', 'action' => 'login'));
			echo '</p>';
		}
	?>
	<hr />
</div>
<?php if (isset($this->Js)) echo $this->Js->writeBuffer(); ?>
</body>
</html>
