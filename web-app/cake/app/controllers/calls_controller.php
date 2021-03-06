<?php 
/*---------------------------------------------------------------------------*
 * controllers/answers_controller.php                                      *
 *                                                                           *
 * Controlls all web-end survey functions at the question level.  All        *
 * functions are ment to be AJAX.                                            *
 *---------------------------------------------------------------------------*/
/**
 * output answers
 * 
 * @author Tony Xiao
 */

class CallsController extends AppController
{
	//for php4
	var $name = 'Calls';
	var $components = array('Auth');

    function rest_index() {
        $this->autoRender = false;
        $this->header('Content-Type: application/json');
        $modelClass = $this->modelClass;
        // add any applicable filters
        $conditions = array();
        if (array_key_exists('filter', $this->params['url'])) {
            $filters = json_decode($this->params['url']['filter'], true);
            foreach ($filters as $filter)
                if (array_key_exists($filter['property'], $this->$modelClass->_schema))
                    $conditions[$modelClass.'.'.$filter['property']] = $filter['value'];
        }

        $models = $this->$modelClass->find('all', array(
            'recursive' => 0,
            'conditions' => $conditions,
            'limit' => 300,
            'order' => 'Call.created DESC'
        ));

        // custom stuff
        $arr = array();
        foreach($models as $item) {
            $item['Call']['subject'] = $item['Subject'];
            $arr[] = $item['Call'];
        }
        e(json_encode($arr));
    }

    /** csv dump */
    function dump() {
        $this->autoRender = false;
        ini_set('max_execution_time', 600); //increase max_execution_time to 10 min if data set is very large
        
        $modelClass = $this->modelClass;
        $filename = $modelClass . "_dump_".date("Y.m.d").".csv";

        header('Content-type: application/csv');
        header('Content-Disposition: attachment; filename="'.$filename.'"');

        // custom stuff
        $csv_file = fopen('php://output', 'w');
        $headers = array('Time', 'Subject Id', 'Subject', 'Contact ID', 'Call Type', 'Call Duration (in seconds)');
        fputcsv($csv_file, $headers, ',', '"');

        $total = $this->$modelClass->find('count');
        $increment = 100;

        for ($offset = 0; $offset<$total; $offset+=$increment) {
            $models = $this->$modelClass->find('all', array(
                'recursive' => 0,
                'order' => 'created DESC',
                'offset' => $offset,
                'limit' => $increment
            ));
            partial_dump($csv_file, $models);
        }
    
        fclose($csv_file);
    }

}

function partial_dump($csv_file, $models) {
    foreach($models as $item) {
        $row = array();
        $row[] = $item['Call']['created']; // Time

        $row[] = $item['Call']['subject_id']; // Subject Id
        $row[] = $item['Subject']['first_name'] ." ". $item['Subject']['last_name']; // Subject
        $row[] = $item['Call']['contact_id']; // Contact Id

        switch ($item['Call']['type']) { // Call type
            case 1:
                $row[] = 'Incoming Call';
                break;
            case 2:
                $row[] = 'Outgoing Call';
                break;
            case 3:
                $row[] = 'Missed Call';
                break;
            case 4:
                $row[] = 'Incoming Text';
                break;
            case 5:
                $row[] = 'Outoing Text';
                break;
            default:
                $row[] = 'Undefined Type';
        }

        $row[] = $item['Call']['duration']; // Call duration
        
        fputcsv($csv_file,$row,',','"');
    }
}

?>