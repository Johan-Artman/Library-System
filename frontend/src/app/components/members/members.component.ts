import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MemberService } from '../../services/member.service';
import { Member, MemberRequest, MemberType } from '../../models/member.model';

@Component({
  selector: 'app-members',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './members.component.html',
  styleUrls: ['./members.component.css']
})
export class MembersComponent implements OnInit {
  members: Member[] = [];
  selectedMember: Member | null = null;
  newMemberRequest: MemberRequest = this.createEmptyMemberRequest();
  showAddForm = false;
  MemberType = MemberType;

  constructor(private memberService: MemberService) { }

  ngOnInit(): void {
    this.loadMembers();
  }

  loadMembers(): void {
    this.memberService.getAllMembers().subscribe({
      next: (members) => {
        this.members = members;
      },
      error: (error) => {
        console.error('Error loading members:', error);
      }
    });
  }

  selectMember(member: Member): void {
    this.selectedMember = { ...member };
  }

  addMember(): void {
    this.memberService.addMember(this.newMemberRequest).subscribe({
      next: (response) => {
        console.log('Member added successfully:', response);
        this.loadMembers(); // Reload the list to get the new member
        this.newMemberRequest = this.createEmptyMemberRequest();
        this.showAddForm = false;
      },
      error: (error) => {
        console.error('Error adding member:', error);
      }
    });
  }

  updateMember(): void {
    if (this.selectedMember) {
      this.memberService.updateMember(this.selectedMember.id, this.selectedMember).subscribe({
        next: (updatedMember) => {
          const index = this.members.findIndex(m => m.id === updatedMember.id);
          if (index !== -1) {
            this.members[index] = updatedMember;
          }
          this.selectedMember = null;
        },
        error: (error) => {
          console.error('Error updating member:', error);
        }
      });
    }
  }

  deleteMember(memberId: number): void {
    if (confirm('Are you sure you want to delete this member?')) {
      this.memberService.deleteMember(memberId).subscribe({
        next: () => {
          this.members = this.members.filter(m => m.id !== memberId);
          if (this.selectedMember && this.selectedMember.id === memberId) {
            this.selectedMember = null;
          }
        },
        error: (error) => {
          console.error('Error deleting member:', error);
        }
      });
    }
  }

  private createEmptyMemberRequest(): MemberRequest {
    return {
      firstname: '',
      lastname: '',
      memberType: MemberType.UNDERGRADUATE,
      socSecNr: ''
    };
  }

  private createEmptyMember(): Member {
    return {
      id: 0,
      firstName: '',
      lastName: '',
      memberType: 1,
      socialSecNr: ''
    };
  }

  cancelEdit(): void {
    this.selectedMember = null;
  }

  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
    if (!this.showAddForm) {
      this.newMemberRequest = this.createEmptyMemberRequest();
    }
  }
}