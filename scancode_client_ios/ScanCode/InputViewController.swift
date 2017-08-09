//
//  InputViewController.swift
//  ScanCode
//
//  Created by Catalin Craciun on 05/02/2017.
//  Copyright © 2017 Catalin Craciun. All rights reserved.
//

import UIKit

class InputViewController: UIViewController {

    @IBOutlet weak var firstNameField: UITextField!
    @IBOutlet weak var lastNameField: UITextField!
    @IBOutlet weak var gendreField: UITextField!

    @IBAction func goPressed(sender: Any) {
        let controller = self.storyboard?.instantiateViewController(withIdentifier: "codeController") as! ViewController
        controller.dataToSave = "\(firstNameField.text!);\(lastNameField.text!);\(gendreField.text!)"
        present(controller, animated: false, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
